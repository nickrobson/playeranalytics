package dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents attributes pertaining to an interaction occurring, which can be used to describe the details of an interaction
 * For example, for a block being placed, the attributes could include the ID of the block that was placed
 */
public class InteractionAttributes {
    private final Map<String, Object> attributes = new LinkedHashMap<>();
    private final Map<String, JsonElement> attributesAsJson = new LinkedHashMap<>();

    private InteractionAttributes doSet(String key, Object value, JsonElement jsonElement) {
        this.attributes.put(key, value);
        this.attributesAsJson.put(key, jsonElement);
        return this;
    }

    private InteractionAttributes removeKey(String key) {
        this.attributes.remove(key);
        this.attributesAsJson.remove(key);
        return this;
    }

    /**
     * Adds a string attribute
     *
     * @param key   The attribute key
     * @param value The value of the attribute
     * @return this attributes object, for simple chaining
     */
    public InteractionAttributes set(String key, String value) {
        return value == null
                ? this.removeKey(key)
                : this.doSet(key, value, new JsonPrimitive(value));
    }

    /**
     * Adds a boolean attribute
     *
     * @param key   The attribute key
     * @param value The value of the attribute
     * @return this attributes object, for simple chaining
     */
    public InteractionAttributes set(String key, boolean value) {
        return this.doSet(key, value, new JsonPrimitive(value));
    }

    /**
     * Adds an integer attribute
     *
     * @param key   The attribute key
     * @param value The value of the attribute
     * @return this attributes object, for simple chaining
     */
    public InteractionAttributes set(String key, int value) {
        return this.doSet(key, value, new JsonPrimitive(value));
    }

    /**
     * Adds an integer attribute
     *
     * @param key   The attribute key
     * @param value The value of the attribute
     * @return this attributes object, for simple chaining
     */
    public InteractionAttributes set(String key, long value) {
        return this.doSet(key, value, new JsonPrimitive(value));
    }

    /**
     * Adds a double or floating point attribute
     *
     * @param key   The attribute key
     * @param value The value of the attribute
     * @return this attributes object, for simple chaining
     */
    public InteractionAttributes set(String key, double value) {
        return this.doSet(key, value, new JsonPrimitive(value));
    }

    /**
     * Adds a string list attribute
     *
     * @param key   The attribute key
     * @param value The value of the attribute
     * @return this attributes object, for simple chaining
     */
    public InteractionAttributes setStringList(String key, List<String> value) {
        JsonArray jsonArray = new JsonArray();
        value.forEach(jsonArray::add);
        return this.doSet(key, value, jsonArray);
    }

    /**
     * Copies all attributes from a given InteractionAttributes object into this object.
     *
     * @param interactionAttributes the other interaction attributes
     * @return this attributes object, for simple chaining
     */
    public InteractionAttributes addAttributes(InteractionAttributes interactionAttributes) {
        this.attributes.putAll(interactionAttributes.attributes);
        this.attributesAsJson.putAll(interactionAttributes.attributesAsJson);
        return this;
    }

    /**
     * Exports the attributes to a {@link Map}, for export into an analytics service or otherwise
     *
     * @return The attributes as a Map
     */
    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Exports the attributes to a {@link JsonObject}, for export into an analytics service or otherwise
     *
     * @return The attributes as a JSON object
     */
    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        attributesAsJson.forEach(jsonObject::add);
        return jsonObject;
    }
}
