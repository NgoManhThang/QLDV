package com.viettel.api.security.xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.apache.cxf.common.util.StringUtils;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

import static org.jsoup.parser.Parser.unescapeEntities;

/**
 * Created by VTN-PTPM-NV19 on 3/27/2018.
 */
@JsonComponent
public class DefaultJsonDeserializer
        extends JsonDeserializer<String>
        implements ContextualDeserializer {

    public static final PolicyFactory POLICY_FACTORY =
            new HtmlPolicyBuilder().allowElements("a", "p")
                    .allowUrlProtocols("https")
                    .allowAttributes("class").onElements("p")
                    .toFactory();
    @Override
    public String deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException {
        String value = parser.getValueAsString();
        if (StringUtils.isEmpty(value) ) return value;
        else                             {
            String originalWithUnescaped  = unescapeUntilNoHtmlEntityFound(value);
            return unescapeEntities(POLICY_FACTORY.sanitize(originalWithUnescaped), true);
        }
    }
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                BeanProperty property)
            throws JsonMappingException {
        return this;
    }

    private String unescapeUntilNoHtmlEntityFound(final String value){
        String unescaped = unescapeEntities(value, true);
        if ( !unescaped.equals(value) ) return unescapeUntilNoHtmlEntityFound(unescaped);
        else                            return unescaped;
    }
}
