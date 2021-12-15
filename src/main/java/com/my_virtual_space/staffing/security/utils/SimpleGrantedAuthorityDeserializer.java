package com.my_virtual_space.staffing.security.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class SimpleGrantedAuthorityDeserializer extends StdDeserializer<Collection<SimpleGrantedAuthority>> {

    public SimpleGrantedAuthorityDeserializer() {
        super(SimpleGrantedAuthority.class);
    }

    @Override
    public Collection<SimpleGrantedAuthority> deserialize(JsonParser p, DeserializationContext context) throws IOException {
        Collection<SimpleGrantedAuthority> collection = new HashSet<>();
        ArrayNode tree = p.getCodec().readTree(p);
        tree.forEach(role -> collection.add(new SimpleGrantedAuthority(role.get("authority").textValue())));
        return collection;
    }
}
