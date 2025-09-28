
import java.util.*;

public class Main{
   public static void main(String[] args) {
      Node root = Admin.loadMenu();

      Scanner sc = new Scanner(System.in);
      while(true){
         System.out.println("1. Admin\n2. Customer\n3. Order History\n4. Exit");
         int choice = sc.nextInt();

         if(choice == 1){
            root = Admin.loadMenu();
            root = Admin.runAdminMenu(root); 
         } else if(choice == 2){
            Customer.runCustomerMenu(root);
         }else if(choice == 3){
            Customer.viewOrderHistory();
         } else {
            break;
         }
      }
   }
}
