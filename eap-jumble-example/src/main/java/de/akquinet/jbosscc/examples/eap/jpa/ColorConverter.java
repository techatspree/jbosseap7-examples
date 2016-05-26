package de.akquinet.jbosscc.examples.eap.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.awt.*;

@Converter//(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {
    @Override
    public String convertToDatabaseColumn(final Color color) {
        return Integer.toHexString(color.getRGB());
    }

    @Override
    public Color convertToEntityAttribute(final String hex) {
        return new Color(Integer.parseInt(hex.substring(0, 6), 16));
    }
}
