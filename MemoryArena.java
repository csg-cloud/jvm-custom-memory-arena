public class MemoryArena {
    private final byte[] memory;
    private int offset = 0;

    public MemoryArena(int size) {
        memory = new byte[size];
    }

    public int alloc(int size) {
        if (offset + size > memory.length) {
            throw new RuntimeException("Out of memory!");
        }
        int start = offset;
        offset += size;
        return start;
    }

    public void reset() {
        offset = 0;
    }

    public int capacity() {
        return memory.length;
    }

    public int used() {
        return offset;
    }

    public int remaining() {
        return memory.length - offset;
    }
}
