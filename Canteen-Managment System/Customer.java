import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
public class Customer {
     
    public static void viewOrderHistory() {
        File myfile = new File("order.txt");
        if (!myfile.exists()) {
            System.out.println("No order history found.");
            return;
        }

        try (Scanner sc = new Scanner(myfile)) {
            System.out.println("------Order History------");
            System.out.println("ID | Name | Price");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if(line.trim().isEmpty()) continue; // skip empty lines
                String[] parts = line.split(",");
                if(parts.length < 3) {
                    System.out.println( line);
                    continue;
                }
                System.out.println(parts[0] + " | " + parts[1] + " | Rs." + parts[2]);
            }
        } catch (Exception e) {
            System.out.println("Error reading order history: " + e.getMessage());
        }
    }

     public static void saveOrderHistory(OrderNode head){
            try (PrintWriter pw = new PrintWriter(new FileWriter("order.txt", true))) { 
                pw.println("\n--- Customer Order ---");
                OrderNode temp = head;
                while(temp != null){
                    pw.println(temp.itemId + "," + temp.itemName + "," + temp.price);
                    temp = temp.next;
                }
            } catch (Exception e){
                System.out.println("Error saving order history: " + e.getMessage());
            }
        }
   
    static class OrderNode {
        public  int itemId;
        public  String itemName;
        public  double price;
        public  OrderNode next;
        OrderNode(int itemId, String itemName, double price){
            this.itemId = itemId;
            this.itemName =itemName;
            this.price=price;   
        }
    }

    static class Queue {
         OrderNode head=null , tail = null;
        
        public boolean isEmpty(){
            return head == null && tail == null;
        }

        public void add(int itemId, String itemName, double price){
            OrderNode newNode = new OrderNode( itemId,  itemName,  price); 
            if (head == null) {  
               head = tail = newNode;
               return;
            }
            tail.next = newNode;
            tail = newNode;
         }

        public void clear(){
            head = tail = null;
        }

        public void saveOrders(String OrderMenu){
            try (PrintWriter pw = new PrintWriter(new FileWriter(OrderMenu))) {
                OrderNode temp = head;
                while(temp != null){
                    pw.println(temp.itemId + "," + temp.itemName + "," + temp.price);
                    temp = temp.next;
                }
            } catch (IOException e){
                System.out.println("Error saving orders: " + e.getMessage());
            }
        }

        public void loadOrders(String filename){
            clear();
            try (Scanner sc = new Scanner(new File(filename))) {
                while(sc.hasNextLine()){
                    String[] parts = sc.nextLine().split(",");
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    add(id, name, price);
                }
            } catch (IOException e){
                System.out.println("No previous orders found.");
            }
        }

        public OrderNode removeByName(String itemName){
            if(isEmpty()){
                System.out.println("Order list is empty!");
            }
            OrderNode prev = null;
            OrderNode current = head;
            while(current != null){
                if (current.itemName.equalsIgnoreCase(itemName)){
                    if(prev == null){
                        head = current.next;
                    }
                    else{
                        prev.next = current.next;
                    }
                    if( current == tail){
                        tail = prev;
                    }
                return current;   
            } 
            prev = current;
            current = current.next;
            }  
            System.out.println("Item not found");
            return null;
            }

        public OrderNode peek(){
                if(isEmpty()){
                    System.out.println("Order list is empty!");
                }
                return head;
            }
        }

        public static Node searchByName(Node root, String name) {
            if (root == null) return null;
            Node leftResult = searchByName(root.left, name);
            if (leftResult != null) return leftResult;
            if (root.itemName.equalsIgnoreCase(name)) {
                return root;
            }
            return searchByName(root.right, name);
        }

        public static void inorder(Node root) {
            if(root == null) return;
            inorder(root.left);
            System.out.println(root.itemId + " | " + root.itemName + " | Rs." +root.price );
            inorder(root.right);
        }

    public static void runCustomerMenu(Node root) {
        Scanner sc = new Scanner(System.in);
        Queue q = new Queue();
      while(true){
        System.out.println("======CUSTOMER MENU======");
        System.out.println("1.View Menu\n2.Place Order\n3.Check Current Order\n4.Delete Item\n5.Generate Bill\n6.Go back\nEnter your choice:");
        int option = sc.nextInt();
        sc.nextLine();

        if(option == 6){
             break;
        }
        switch(option){
            case 1:
            System.out.println("------View Menu-----");
            System.out.println("ID | Name | Price");
            inorder(root);
            break;
            case 2:
            System.out.println("------Place Order-----");
            System.out.println("Enter item Name:");
            String orderName = sc.nextLine();
            Node found = searchByName(root, orderName);
            if(found!= null){
                q.add(found.itemId,found.itemName, found.price);
                System.out.println("Item Added is: " + found.itemName);
            } else {
                System.out.println("Item not found!");
            }
            break;
            case 3:
            System.out.println("------Current Order-----");
                if (q.head == null) {
                System.out.println("No current orders.");
            } else {
                OrderNode temp = q.head;
                while (temp != null) {
                    System.out.println(temp.itemId + " | " + temp.itemName + " | Rs." + temp.price);
                    temp = temp.next;
                }
            }
            break;
            case 4:
            System.out.println("------Delete Item-----");
            if (q.head == null) {
                System.out.println("No current orders.");
            } else {
                System.out.println("Enter item Name to remove:");
                String deleteOrder = sc.nextLine();
                OrderNode founditem = q.removeByName(deleteOrder); 
                if (founditem != null) {
                    System.out.println("Item Removed: " + founditem.itemName);
                } else {
                    System.out.println("Item not found!");
                }
            }
            break;
            case 5 :
            System.out.println("------Generated Bill-----");
                if (q.head == null) {
                System.out.println("No current orders.");
            } else {
                OrderNode temp = q.head;
                double totalPrice = 0;
                while (temp != null) {
                    System.out.println(temp.itemId + " | " + temp.itemName + " | Rs." + temp.price);
                    totalPrice += temp.price;
                    temp = temp.next;
                } 
                System.out.println("Total is :Rs."+ totalPrice);
                saveOrderHistory(q.head);
            }
            break;
            default:
            System.out.println("Go back......");
            System.out.println(" ");
        
        }
    }
        
    }
}