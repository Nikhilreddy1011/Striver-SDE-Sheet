class Solution {
    private boolean isEqual(char i, char j){
        if((i == '{' && j == '}') ||(i == '[' && j == ']') || (i == '(' && j == ')') ){
            return true;
        }
        return false;
    }
    public boolean isValid(String str) {
        int n = str.length();
        Stack<Character> box = new Stack<>();
        for(int i = 0 ; i < n ; i++){
            char ch = str.charAt(i);
            if(str.charAt(i)== '{' || str.charAt(i) == '[' || str.charAt(i) == '('){
                box.push(ch);
            }
            else{
                if(box.isEmpty()) return false;
                char top = box.pop();
                if(!isEqual(top,ch)){
                    return false;
                }

            }

        }

        return box.isEmpty();
        
    }
}