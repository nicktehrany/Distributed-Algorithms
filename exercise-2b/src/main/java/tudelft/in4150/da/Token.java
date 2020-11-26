package tudelft.in4150.da;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Token class which is used as the Vector token for each of the processes and the timestamps in messages.
 */
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private int[] ln;
    private Queue<Integer> queue;

    /**
     * Token constructor, initialize token with 0s on Vectrotoken construction.
     *
     * @param processes
     */
    public Token(int processes) {
        ln = new int[processes];
        Arrays.fill(ln, 0);
        queue = new LinkedList<>();
    }

    /**
     * Token copy constructor with new ln length.
     *
     * @param token
     */
    public Token(Token token, int length) {
        int[] temp = new int[length];
        Arrays.fill(temp, 0);
        for (int i = 0; i < token.ln.length; i++) {
            temp[i] = token.ln[i];
        }
        ln = temp;
    }

    /**
     * Get the counter of the token satisfied requests for a process.
     *
     * @param index
     */
    public int getValue(int index) {
        return ln[index];
    }

    /**
     * Set the token satisfied request counter.
     * @param index
     * @param value
     */
    public void setValue(int index, int value) {
        ln[index] = value;
    }

    /**
     * Returns the tokens' satisfied requests as a printable string.
     * @return
     */
    public String getRequests() {
        return Arrays.toString(ln);
    }

    /**
     * Returns the length of the tokens' satisfied requests.
     * @return
     */
    public int getLength() {
        return ln.length;
    }

    /**
     * Returns the queue as printable string.
     * @return
     */
    public String getQueue() {
        return queue.toString();
    }

    /**
     * Enqueues the provided element in the queue.
     * @param element
     */
    public void enqueue(int element) {
        queue.add(element);
    }

    /**
     * Gets and returns the element at the head of the requests queue.
     * @return head
     */
    public int getQueueHead() {
        int element = queue.peek();
        queue.remove(element);
        return element;
    }

    /**
     * Checks if the queue of requests is empty.
     * @return
     */
    public boolean queueIsEmpty() {
        return queue.isEmpty();
    }

}
