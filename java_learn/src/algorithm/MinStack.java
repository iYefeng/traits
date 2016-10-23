package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class MinStack {
    Stack dataStack = null;
    Stack minStack = null;
    public MinStack() {
        dataStack = new Stack();
        minStack = new Stack();
    }

    public void push(int data) {
        dataStack.push(data);
        if (minStack.isEmpty()) {
            minStack.push(data);
        } else {
            if (minStack.peek().data_ >= data) {
                minStack.push(data);
            }
        }
    }

    public Node pop() {
        Node tmp = dataStack.pop();
        if (tmp == null)
            return null;
        if (tmp.data_ == minStack.peek().data_) {
            minStack.pop();
        }
        return tmp;
    }

    public Node min() {
        return minStack.peek();
    }

    public static void main(String[] args) {
        MinStack ms = new MinStack();
        ms.push(11);
        ms.push(14);
        ms.push(2);
        ms.push(1);
        ms.push(34);
        ms.push(1);

        Node tmp = ms.min();
        if (tmp != null) {
            System.out.println(tmp.data_);
        }
        ms.pop();
        ms.pop();
        ms.pop();
        tmp = ms.min();
        if (tmp != null) {
            System.out.println(tmp.data_);
        }
    }

}
