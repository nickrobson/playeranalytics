package dev.nickrobson.minecraft.playeranalytics.core.api.interaction;

import javax.annotation.Nonnull;

/**
 * Represents an interaction of some sort
 */
public interface Interaction {

    /**
     * The thing that is being interacted with, for example a block, item, or entity
     *
     * @return the subject of the interaction
     */
    @Nonnull
    String getSubject();

    /**
     * The action being performed, such as "placed" or "broken" if the user placed/broke a block.
     * Should be an English verb in past tense.
     *
     * @return the action as a past tense verb
     */
    @Nonnull
    String getAction();
}
