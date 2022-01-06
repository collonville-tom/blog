package exemple.dixsept.seal;

public sealed class AbstractSealed permits A, AbstractSealed.B,C {

    public final class B extends AbstractSealed{
    }
}

