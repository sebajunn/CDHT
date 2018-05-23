/*     */ import java.io.PrintStream;
/*     */ import java.util.Scanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class cdht_ex$6
/*     */   extends Thread
/*     */ {
/*     */   public cdht_ex$6(Thread paramThread1, Thread paramThread2, Thread paramThread3, Thread paramThread4, Thread paramThread5) {}
/*     */   
/*     */   public void run()
/*     */   {
/* 323 */     Scanner sc = new Scanner(System.in);
/*     */     for (;;)
/*     */     {
/* 326 */       String inputString = sc.nextLine();
/* 327 */       if (inputString.equals("")) {
/* 328 */         if ((cdht_ex.userInput) || (cdht_ex.requestFileMessage)) {
/* 329 */           cdht_ex.userInput = false;
/* 330 */           cdht_ex.requestFileMessage = false;
/* 331 */           System.out.println("You have pressed enter key, ping messages are displayed again\n");
/*     */ 
/*     */         }
/* 334 */         else if (!cdht_ex.userInput) {
/* 335 */           cdht_ex.userInput = true;
/* 336 */           System.out.println("You have pressed enter key, ping messages are hide\n");
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 341 */       else if (inputString.equals("quit"))
/*     */       {
/*     */ 
/*     */ 
/* 345 */         String sendMessage = "quit " + cdht_ex.peerIdentity + " " + cdht_ex.firstSuccessor + " " + cdht_ex.secondSuccessor + "\n";
/* 346 */         try { int i = -25;int j = i - 1;int k = i * j;k %= 2; if (((k < 0 ^ true) & true)) i = -53; cdht_ex.M597545277(sendMessage, cdht_ex.firstSuccessorPort, i); } catch (Exception e) { return; }
/* 347 */         try { Thread.currentThread();Thread.sleep(3000L); } catch (InterruptedException e) { return;
/*     */         }
/* 349 */         this.val$firstClient.interrupt();
/* 350 */         this.val$secondClient.interrupt();
/* 351 */         this.val$check.interrupt();
/* 352 */         if (cdht_ex.count == 2) {
/* 353 */           this.val$newServer.interrupt();
/* 354 */           this.val$requestFileServer.interrupt();
/*     */         }
/*     */       }
/*     */       else {
/* 358 */         String command = inputString.split(" ")[0];
/* 359 */         if (command.equals("request"))
/*     */         {
/*     */ 
/*     */ 
/* 363 */           String fileName = inputString.split(" ")[1];
/* 364 */           int m = -54;int n = m - 1;int i1 = m * n;i1 %= 2; if (((i1 < 0 ^ true) & true)) m = -101; int hashValue = cdht_ex.M240536415(fileName, m);
/* 365 */           String sendMessage = "request " + fileName + " " + hashValue + " " + cdht_ex.peerIdentity + " " + cdht_ex.peerIdentity + "\n";
/*     */           try {
/* 367 */             int i2 = 46;int i3 = i2 - 1;int i4 = i2 * i3;i4 %= 2; if (((i4 < 0 ^ true) & true)) i2 = 1; cdht_ex.M597545277(sendMessage, cdht_ex.firstSuccessorPort, i2); } catch (Exception e) { return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\COMP3331\Normal\cdht_ex_obf.jar!\cdht_ex$6.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */