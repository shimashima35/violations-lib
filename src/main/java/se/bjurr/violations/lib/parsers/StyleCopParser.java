package se.bjurr.violations.lib.parsers;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static se.bjurr.violations.lib.model.SEVERITY.INFO;
import static se.bjurr.violations.lib.model.Violation.violationBuilder;
import static se.bjurr.violations.lib.parsers.ViolationParserUtils.getAttribute;
import static se.bjurr.violations.lib.parsers.ViolationParserUtils.getIntegerAttribute;
import static se.bjurr.violations.lib.reports.Reporter.STYLECOP;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;

public class StyleCopParser implements ViolationsParser {

  @Override
  public List<Violation> parseReportOutput(String string) throws Exception {
    List<Violation> violations = new ArrayList<>();

    try (InputStream input = new ByteArrayInputStream(string.getBytes())) {

      XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLStreamReader xmlr = factory.createXMLStreamReader(input);

      while (xmlr.hasNext()) {
        int eventType = xmlr.next();
        if (eventType == START_ELEMENT) {
          if (xmlr.getLocalName().equals("Violation")) {
            String section = getAttribute(xmlr, "Section");
            String source = getAttribute(xmlr, "Source");
            String ruleNamespace = getAttribute(xmlr, "RuleNamespace");
            String rule = getAttribute(xmlr, "Rule");
            String ruleId = getAttribute(xmlr, "RuleId");
            Integer lineNumber = getIntegerAttribute(xmlr, "LineNumber");
            String message = xmlr.getElementText().replaceAll("\\s+", " ");
            SEVERITY severity = INFO;
            String filename = source.replaceAll("\\\\", "/");
            violations.add( //
                violationBuilder() //
                    .setReporter(STYLECOP) //
                    .setMessage(message) //
                    .setFile(filename) //
                    .setStartLine(lineNumber) //
                    .setRule(rule) //
                    .setSeverity(severity) //
                    .setSource(filename) //
                    .setSpecific("section", section) //
                    .setSpecific("source", source) //
                    .setSpecific("ruleNamespace", ruleNamespace) //
                    .setSpecific("rule", rule) //
                    .setSpecific("ruleId", ruleId) //
                    .build() //
                );
          }
        }
      }
    }
    return violations;
  }
}
