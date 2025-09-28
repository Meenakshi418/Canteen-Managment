
   public class Node {
      public  int itemId;
      public  String itemName;
      public  double price;

     public   Node left;
     public   Node right;
        Node(int itemId, String itemName, double price){
            this.itemId = itemId;
            this.itemName =itemName;
            this.price=price;
            
        }
    }