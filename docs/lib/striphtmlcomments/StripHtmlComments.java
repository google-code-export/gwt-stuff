import java.io.FilterReader;
import java.io.Reader;
import java.io.IOException;

/**
 * Empties &lt;-- comments -- &gt; from files.
 * This is to minimize the diff in generated javadocs.
 *
 * @author Sandy McArthur
 */
public class StripHtmlComments extends FilterReader {
    /**
     * Create a new filtered reader.
     *
     * @param in a Reader object providing the underlying stream.
     * @throws NullPointerException if <code>in</code> is <code>null</code>
     */
    public StripHtmlComments(final Reader in) {
        super(in);
    }


    private boolean inComment = false;
    private boolean exitingComment = false;
    private int c = 0;

    /**
     * Read a single character.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    public int read() throws IOException {
        if (!inComment){
            final int ch = super.read();
            if (c == 0 && ch == '<') {
                c = 1;
            } else if (c == 1 && ch == '<') {
                // adjacent < chars. eg: <<--
                // don't advance c
            } else if (c == 1 && ch == '!') {
                c = 2;
            } else if (c == 2 && ch == '-') {
                c = 3;
            } else if (c == 3 && ch == '-') {
                inComment = true;
            } else {
                c = 0;
            }
            return ch;
        } else {
            if (!exitingComment) {
                final StringBuffer sb = new StringBuffer(4);
                while (sb.lastIndexOf("-->") == -1) {
                    final int ch = super.read();
                    if (ch == -1) {
                        return ch;
                    }
                    sb.append((char)ch);
                }
                sb.setLength(0);

                exitingComment = true;
            }
            switch (c) {
                case 3:
                case 2:
                    c--;
                    return '-';

                case 1:
                    c = 0;
                    inComment = exitingComment = false;
                    return '>';
                default:
                    throw new IllegalStateException("Shouldn't get here: " + c);
            }
        }
    }

    /**
     * use {@link #read()}  
     */
    public final int read(final char[] cbuf, final int off, final int len) throws IOException {
        for (int i = 0; i < len; i++) {
            final int ch = read();
            if (ch == -1) {
                if (i == 0) {
                    return -1;
                } else {
                    return i;
                }
            }
            cbuf[off + i] = (char) ch;
        }
        return len;
    }
}
