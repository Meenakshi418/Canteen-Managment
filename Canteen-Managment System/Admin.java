import java.io.File;
import java.io.PrintWriter;
import java.util.*;
public class Admin {
    public static int safeIntInput(Scanner sc, String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine(); // clear bad input
            }
        }
    }

    public static double safeDoubleInput(Scanner sc, String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine(); // clear bad input
            }
        }
    }

    public static int countMenu(Node root) { 
        if (root == null) return 0;
        return 1 + countMenu(root.left) + countMenu(root.right);
    }

    public static void saveMenu(Node root){
        try(PrintWriter pw = new PrintWriter("menu.txt")){
            saveInorder(root,pw);
        } catch (Exception e){
            System.out.println("Error saving the menu:"+ e.getMessage());
        }
    }

    public static void saveInorder(Node root, PrintWriter pw){
        if (root == null) return;
        saveInorder(root.left , pw);
        pw.println(root.itemId + "," + root.itemName + "," + root.price);
        saveInorder(root.right , pw);
    }

    public static Node loadMenu(){
        Node root = null;
        try(Scanner sc = new Scanner(new File("menu.txt"))){while (sc.hasNextLine()) {
            String[] parts = sc.nextLine().split(",");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double price = Double.parseDouble(parts[2]);
            root = insert(root, id, name, price);
            }
        } catch(Exception e){
            System.out.println("No saved menu . Starting Fresh.");
        }
        return root;
        }

    public static Node insert(Node root,int itemId, String itemName, double price){
        if (root == null){
            return  new Node( itemId, itemName, price);
        }
        
        if (root.itemId> itemId){
          root.left = insert(root.left, itemId, itemName, price);  
        }
        else {
          root.right = insert(root.right, itemId, itemName, price);  
        }
        return root;
    }

     public static void inorder(Node root) {
        if(root == null) return;
        inorder(root.left);
        System.out.println(root.itemId + " | " + root.itemName + " | Rs." +root.price );
        inorder(root.right);
     }

     public static boolean update(Node root,int itemId, String newName, double newPrice){
        if(root == null){  // case 1: empty tree
           System.out.println("Item not found!.");
           return false;       
        }
        if(root.itemId > itemId){ // case 2: search in left subtree
            return update(root.left, itemId, newName, newPrice);
        }
        else if(root.itemId < itemId){  // case 3: search in right subtree
            return update(root.right, itemId, newName, newPrice);
        }
        else {
            // found the item
            root.itemName = newName;
            root.price = newPrice;
            return true;
        }
    }

    public static Node delete(Node root ,int itemId, String itemName, double price ) {
         if (root == null) {
            return root; // case 1: empty tree
        }

        if(root.itemId > itemId){ // case 2: search in left subtree
            root.left = delete(root.left,itemId, itemName, price);
        }
        else if(root.itemId < itemId){ // case 3: search in right subtree
            root.right = delete(root.right,itemId, itemName, price);
        }
        else {
             // found node to delete
            if(root.left == null && root.right == null){
                return null;
            }

            if(root.left == null ){
                return root.right;
            }
            else if(root.right == null ){
                return root.left;
            }

            Node IS = inordersuccesor(root.right);
            // copy successor's data
            root.itemId = IS.itemId;
            root.itemName = IS.itemName;
            root.price = IS.price;
            // delete the successor node
            root.right = delete(root.right,IS.itemId, IS.itemName, IS.price);
        }
        return root;
    }
    
    public static Node inordersuccesor(Node root){
        while(root.left != null){
           root = root.left;
        }
        return root;
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
    
    public static Node searchById(Node root, int id) {
        if (root == null) return null;
        if (root.itemId == id) return root;
        if (id < root.itemId) return searchById(root.left, id);
        return searchById(root.right, id);
    }

    public static Node runAdminMenu(Node root) {
        Scanner sc = new Scanner(System.in);
         root = loadMenu(); // load from file before using inorder
      while(true){
        System.out.println("======ADMIN MENU======");
        System.out.println("1.Add\n2.Delete\n3.Update\n4.View Menu\n5.Go back\nEnter your choice:");
        int option = sc.nextInt();
        sc.nextLine();

        if(option == 5){
             break;
        }
        switch(option){
            case 1:
            inorder(root);
            System.out.println("------Add Item-----");
            
            System.out.print("Enter number of items to add: " );
            int n = sc.nextInt();
            sc.nextLine(); 
            
                for (int i = 0; i < n; i++) {
                System.out.println("Enter item details:");
                    
                System.out.print("Enter Item Name (or 0 to exit): ");
                String name = sc.nextLine();

                    if (name.equals("0")) {
                        System.out.println("Stopped adding.");
                        break;
                    }
                    Node found = searchByName(root, name);
                    if(found != null){
                        System.out.println("An item with that name already exists. Try again.");
                        continue;
                    } 

                int id = safeIntInput(sc, "Enter Item ID: ");
                    sc.nextLine();
                    if (id<=0) {
                            System.out.println("Invalid id.");
                            i--;
                            continue;
                        }
                    if (searchById(root, id) != null) {
                        System.out.println("Item ID already exists. Try again.");
                        continue;
                    }

                double price = safeDoubleInput(sc, "Enter Price: ");
                sc.nextLine();
                    if (price<=0) {
                        System.out.println("Invalid price. Item can not be free/negative.");
                        i--;
                        continue;
                    }

                root = insert(root, id, name, price);
                System.out.println("Item added.");
                }
                saveMenu(root);

            break;

            case 2:
            inorder(root);
            System.out.println("------Delete Item-----");
            System.out.print("Enter number of items to delete: ");
            int m = sc.nextInt();
            sc.nextLine(); 

            if( m<=countMenu(root)){
                for (int i = 0; i < m; i++) {
                System.out.println("Enter item details:");

                System.out.print("Enter Item Name (or 0 to exit): ");
                String name = sc.nextLine();
                if (name.equalsIgnoreCase("0")) {
                    System.out.println("Exiting...");
                    continue; 
                }
                Node found = searchByName(root, name);
                if(found != null){
                    root = delete(root, found.itemId, found.itemName,found.price);
                } else {
                    System.out.println("Item doesn't exist");
                }
                System.out.println("Item deleted.");
                }
                saveMenu(root);
                } else {
                System.out.println("Entered no. is greater than the no. of menu items");
            }

            break;
            case 3:
            inorder(root);
            System.out.println("------Update Item-----");
            System.out.print("Enter number of items to Update: ");
            int o = sc.nextInt();
            sc.nextLine(); 
            if( o<=countMenu(root)){
                for (int i = 0; i < o; i++) {
                    int id = safeIntInput(sc, "Enter Item ID: ");
                    sc.nextLine(); 
                        if (id<=0) {
                            System.out.println("Invalid id.");
                            i--;
                            continue;
                        }
                    System.out.print("Enter new/old Item Name: ");
                    String newname = sc.nextLine(); 
                    Node existing = searchByName(root, newname);
                    if (existing != null && existing.itemId != id) {
                        System.out.println("Item Name already exists. Try again.");
                        continue;
                    }

                    double newprice = safeDoubleInput(sc, "Enter New/old Price: ");
                        if (newprice<=0) {
                            System.out.println("Invalid price. Item can not be free/negative.");
                            i--;
                            continue;
                        }

                    boolean updated = update(root, id, newname, newprice);
                    if (updated) {
                        System.out.println("Item updated successfully!");
                    } else {
                        System.out.println("No item with ID " + id + " found.");
                    }
                }
                saveMenu(root);
            } 
            else {
                System.out.println("Entered no. is greater than the no. of menu items");
            }
            
            break;
            case 4 :
            System.out.println("------View Menu-----");
            System.out.println("\nView Menu :");
            System.out.println("ID | Name | Price");
            inorder(root);
            break;
            default:
            System.out.println("Go back......");
            System.out.println(" ");
        }
    }
    return root;     
    }
}