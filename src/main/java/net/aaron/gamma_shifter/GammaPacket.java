package net.aaron.gamma_shifter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A pseudo-packet that contains a new gamma value and information about the class that sent the packet. The sender
 * information is stored as an enum and delivered within an {@link Optional}.
 * <p>The sender information is nullable, and an empty Optional is considered valid to be contained in a packet.</p>
 * <p>The value within the packet may not be null.</p>
 * <p>A packet may not be instantiated without initializing all fields.</p>
 */
public class GammaPacket {
    /**
     * The included gamma value. This value may never be null.
     */
    public Double value;

    /**
     * Information about the packet sender as an enumerated value.
     * @see Sender
     */
    private @Nullable Sender sender;

    /**
     * Enums used to label packet senders.
     */
    public enum Sender{
        AUTO_NIGHT
    }

    /**
     * A packet may not be instantiated without initializing all fields.
     */
    private GammaPacket(){}

    /**
     * Considered the default constructor for a packet. All fields must be instantiated.
     * @param value The value to store in the packet. May never be null.
     * @param sender The sender of the package as a {@link Sender} enum.
     * @see Sender
     */
    public GammaPacket(@NotNull Double value, @Nullable Sender sender){
        this.value = value;
        this.sender = sender;
    }

    /**
     * Gets an {@link Optional} with enumerated information about the packet sender. An empty Optional is considered a valid return
     * value.
     * @return The Optional containing enum info about the packet sender. May be empty.
     */
    public Optional<Sender> sender(){
        return this.sender!=null ? Optional.of(this.sender) : Optional.empty();
    }
}
