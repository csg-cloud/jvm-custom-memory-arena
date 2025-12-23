public class StringStore {
    private final MemoryArena arena;
    private static final int LENGTH_OFFSET = 0;
    private static final int DATA_OFFSET = 4;
    private static final int CHAR_SIZE = 2;

    public StringStore(MemoryArena arena) {
        this.arena = arena;
    }

    public int createString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        
        int length = s.length();
        int totalSize = DATA_OFFSET + (length * CHAR_SIZE);
        int stringAddr = arena.alloc(totalSize);
        
        arena.putInt(stringAddr + LENGTH_OFFSET, length);
        
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            int charAddr = stringAddr + DATA_OFFSET + (i * CHAR_SIZE);
            arena.putChar(charAddr, c);
        }
        
        return stringAddr;
    }

    public String getString(int stringAddr) {
        checkStringPtr(stringAddr);
        int length = getStringLength(stringAddr);
        
        if (length == 0) {
            return "";
        }
        
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            int charAddr = stringAddr + DATA_OFFSET + (i * CHAR_SIZE);
            chars[i] = arena.getChar(charAddr);
        }
        
        return new String(chars);
    }

    public int getStringLength(int stringAddr) {
        checkStringPtr(stringAddr);
        return arena.getInt(stringAddr + LENGTH_OFFSET);
    }

    public char getCharAt(int stringAddr, int index) {
        checkStringPtr(stringAddr);
        int length = getStringLength(stringAddr);
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for string of length " + length
            );
        }
        
        int charAddr = stringAddr + DATA_OFFSET + (index * CHAR_SIZE);
        return arena.getChar(charAddr);
    }

    public void setCharAt(int stringAddr, int index, char c) {
        checkStringPtr(stringAddr);
        int length = getStringLength(stringAddr);
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for string of length " + length
            );
        }
        
        int charAddr = stringAddr + DATA_OFFSET + (index * CHAR_SIZE);
        arena.putChar(charAddr, c);
    }

    public void printString(int stringAddr) {
        checkStringPtr(stringAddr);
        String s = getString(stringAddr);
        System.out.println("\"" + s + "\" (length: " + getStringLength(stringAddr) + ")");
    }

    private void checkStringPtr(int ptr) {
        if (ptr < 0) {
            throw new InvalidPointerException(ptr, DATA_OFFSET, arena.used(), arena.capacity());
        }
        if (ptr + DATA_OFFSET > arena.used()) {
            throw new InvalidPointerException(ptr, DATA_OFFSET, arena.used(), arena.capacity());
        }
    }
}

