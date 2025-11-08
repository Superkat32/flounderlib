package net.superkat.flounderlib.api.text;

import net.superkat.flounderlib.api.text.type.FlounderTextParams;

public interface FlounderTextFactory<T extends FlounderTextParams> {
    FlounderText createText(T params);
}
