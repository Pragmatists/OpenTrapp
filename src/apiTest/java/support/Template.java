package support;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import org.concordion.api.Resource;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.DocumentParsingListener;
import org.concordion.internal.listener.AssertResultRenderer;

public class Template implements ConcordionExtension{

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        
        IncludesJsonCommand command = new IncludesJsonCommand();
        command.addAssertEqualsListener(new AssertResultRenderer());
        concordionExtender.withCommand("https://github.com/mpi/OpenTrApp", "assertIncludesJson", command);
        concordionExtender.withLinkedCSS("/support/concordion.css", new Resource("/concordion.css"));
        concordionExtender.withResource("/support/index.html", new Resource("/index.html"));
        concordionExtender.withDocumentParsingListener(new DocumentParsingListener() {
            
            @Override
            public void beforeParsing(Document document) {
                
                Element header = new Element("div");
                header.addAttribute(new Attribute("class", "header"));
                header.appendChild(title("TrApp - Time Registration Application"));
                header.appendChild(subtitle("REST API Specification"));
                Element body = (Element) document.query("//body").get(0);
                body.insertChild(header, 0);
            }

            private Element title(String value) {
                Element title = new Element("h1");
                title.appendChild(value);
                return title;
            }
            private Element subtitle(String value) {
                Element title = new Element("p");
                title.appendChild(value);
                return title;
            }
        });
        
    }

}
