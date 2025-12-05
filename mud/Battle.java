package cn.edu.usst.mud;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

import static cn.edu.usst.input.Input.scanner;

//战斗场
public class Battle extends Scene {
    private Scanner scannner = new Scanner(System.in);

    public Battle() {
        super("编程实战基地");
    }

    public void enter(Programmer programmer) {
        System.out.println("\n=== 进入 " + sceneName + " ===");

        // 从文本文件随机抽取一个 Bug
        List<Bug> bugs = GameDataHandler.loadBugs();
        Bug bug;
        if (bugs.isEmpty()) {
            bug = new Bug("野生空指针", 30);
        } else {
            bug = bugs.get(new Random().nextInt(bugs.size()));
        }

        System.out.println("警告！遭遇了 " + bug.getName() + " (HP: " + bug.getHp() + ")");
        System.out.println("战斗开始！");

        //战斗循环
        while (programmer.isAlive() && bug.isAlive()) {
            System.out.println("\n--- 回合开始 [你: " + programmer.getHp() + " HP] vs [" + bug.getName() + ": " + bug.getHp() + " HP] ---");
            System.out.println("1. 进攻 (使用技能)");
            System.out.println("2. 防御 (减少50%伤害)");
            System.out.println("3. 尝试躲避 (40%几率无伤，失败则承伤)");
            System.out.println("4. 逃跑");
            System.out.print("请选择策略: ");

            String choice = scanner.nextLine();
            boolean playerActed = false;
            boolean isDefending = false;
            boolean tryDodge = false;

            //---玩家回合---
            switch (choice) {
                case "1": // 攻击
                    if (performAttack(programmer, bug)) {
                        playerActed = true;
                    }
                    break;
                case "2": // 防御
                    System.out.println(">> 你架起键盘，准备抵挡接下来的报错！");
                    isDefending = true;
                    playerActed = true;
                    break;
                case "3": // 躲避
                    System.out.println(">> 你试图预判 Bug 的走位...");
                    tryDodge = true;
                    playerActed = true;
                    break;
                case "4": // 逃跑
                    System.out.println(">> 你拔掉网线，溜之大吉！");
                    return; // 退出方法即退出战斗
                default:
                    System.out.println("无效指令，浪费了一个回合！");
                    playerActed = true; // 即使输入错误也算过回合
            }
            // --- Bug 回合 ---
            if (bug.isAlive() && playerActed) {
                handleBugAttack(bug, programmer, isDefending, tryDodge);
            }


            // 3. 结算
            if (programmer.isAlive()) {
                System.out.println("\nVICTORY! 你成功修复了 " + bug.getName());
                programmer.completeBattle();
            } else {
                System.out.println("\nDEFEAT... 你的屏幕变黑了，发量归零。");
            }

            printFooter();
        }
//    public static void action(Programmer programmer, DigitalLife bug){
//        //玩家攻击
//        System.out.println("请选择技能：");
//        List <Skill> skills=programmer.getSkills();

    }

    private boolean performAttack(Programmer p, Bug b) {
        System.out.println("请选择技能:");
        List<Skill> skills = p.getSkills();
        for (int i = 0; i < skills.size(); i++) {
            System.out.println((i + 1) + ". " + skills.get(i).getName() + " (Lv." + skills.get(i).getLevel() + ")");
        }
        System.out.print("选择: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < skills.size()) {
                Skill s = skills.get(idx);
                if (s.isHit()) {
                    int dmg = s.calculateDamage();
                    System.out.println(">> 你的 [" + s.getName() + "] 击中了 Bug！");
                    b.takeDamage(dmg);
                } else {
                    System.out.println(">> 你的 [" + s.getName() + "] 被 Bug 拦截了 (Miss)！");
                }
                return true;
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        System.out.println("选择无效，这回合发呆了。");
        return true;
    }

    private void handleBugAttack(Bug bug, Programmer p, boolean isDefending, boolean tryDodge) {
        System.out.println("\n[敌方行动] " + bug.getName() + " 发起进攻！");

        // 躲避判定 (假设基础躲避率+尝试躲避加成)
        boolean dodgeSuccess = tryDodge && (new Random().nextInt(100) < 40); // 40%几率手动躲避

        if (dodgeSuccess) {
            System.out.println("   >> MISS! 你的神走位完全避开了攻击！");
            return;
        } else if (tryDodge) {
            System.out.println("   >> 哎呀！躲避失败，脸接了 Bug！");
        }

        int damage = bug.attack();

        if (isDefending) {
            damage /= 2;
            System.out.println("   >> 防御生效！伤害减半。");
        }

        p.takeDamage(damage);

    }
}

//    //Bug攻击逻辑
//    private static void bugAttack(Bug bug, Programmer programmer){
//        //玩家躲避
//        if(programmer.dodge()){
//            System.out.println("太帅了！你成功躲避了Bug的报错攻击！");
//        }
//        else{
//            int damage=bug.attack();
//            System.out.println(new StringBuilder().append(bug.getName()).append(" 命中了你，发量 -").append(damage).toString());
//            programmer.takeDamage(damage);
//        }
//    }

