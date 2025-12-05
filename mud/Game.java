package cn.edu.usst.mud;

import java.io.*;
import java.util.Scanner;
import java.io.Serializable;

public class Game {
    private static Programmer programmer;//玩家唯一且全程存在，静态变量生命周期与程序运行周期相同

    public static void main(String[]args){
        Scanner scanner = new Scanner(System.in);

        // 文本读取
        programmer = GameDataHandler.loadPlayer();
        //防止文件为空的设定
        if (programmer == null) {
            System.out.println("=== Java MUD Game ===");
            System.out.println("未检测到存档，请输入你的名字创建新角色：");
            String name = scanner.nextLine();
            programmer = new Programmer(name, 50); // 默认50血
            System.out.println("角色创建成功！");
        } else {
            System.out.println("=== 欢迎回来，" + programmer.getName() + " ===");
        }

        //游戏主循环
        boolean running=true;//退出游戏时好操作
        while(running){
            System.out.println("\n################ 游戏大厅 ################");
            System.out.println("玩家: " + programmer.getName());
            System.out.println("状态: LV." + programmer.getLevel() + " | HP: " + programmer.getHp() + "/" + programmer.getMaxHp());
            System.out.println("技能: " + programmer.getSkills().size() + " 个");
            System.out.println("----------------------------------------");
            System.out.println("1. 进入图书馆 (训练/回血)");
            System.out.println("2. 进入编程实战 (打怪/升级)");
            System.out.println("3. 保存并退出");
            System.out.print("请选择指令: ");

            String choice =scanner.nextLine();
            switch(choice){
                case"1":
                    Library library=new Library();
                    new Library().enter(programmer);
                    break;
                case"2":
                    if(programmer.isAlive()){
                        new Battle().enter(programmer);
                    }
                    else {
                        System.out.println(">> 你已经秃顶(HP=0)了，请先去图书馆植发(回血)！");
                    }
                    break;
                case"3":
                    GameDataHandler.savePlayer(programmer);
                    System.out.println("再见，期待你的下一次提交！");
                    running = false;
                    break;
                default:
                    System.out.println("无效指令，请重新选择。");
            }
            //复活机制，防止游戏进入死循环
            if(!programmer.isAlive()&&!choice.equals("3")){
                System.out.println(">> 警告：你已经秃顶了（死亡），去图书馆可以恢复发量。");
            }
        }


}

}

