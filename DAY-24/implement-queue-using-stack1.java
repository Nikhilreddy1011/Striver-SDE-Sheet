class StackQueue {
    private Stack<Integer> input,output;

    public StackQueue() {
        input = new Stack<>();
        output = new Stack<>();
    }

    public void push(int x) {
       input.push(x);
    }

    public int pop() {
      if(output.isEmpty()){
        while(!input.isEmpty()){
            output.push(input.pop());
        }
      }
      if(output.isEmpty()){
        System.out.println("Queue is empty");
        return -1;
      }
      return output.pop();
    }

    public int peek() {
              if(output.isEmpty()){
        while(!input.isEmpty()){
            output.push(input.pop());
        }
      }
      if(output.isEmpty()){
        System.out.println("Queue is empty");
        return -1;
      }
      return output.peek();
    }

    public boolean isEmpty() {
        return input.isEmpty() && output.isEmpty();
    }
}
