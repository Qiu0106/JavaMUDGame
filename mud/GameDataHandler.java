package cn.edu.usst.mud;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameDataHandler {

    // 读取怪物配置 (bug.txt)
    public static List<Bug> loadBugs() {
        List<Bug> list = new ArrayList<>();
        File file = new File("bug.txt");
        if (!file.exists()) return list; // 文件不存在返回空列表
        //try-with-resources 自动关闭流
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 格式: 名字,生命值
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String name = parts[0];
                    int hp = Integer.parseInt(parts[1]);
                    list.add(new Bug(name, hp));//用解析后的名字和生命值创建Bug对象，添加到列表中。
                }//校验分割后的数组长度（至少包含 “名字” 和 “生命值”），避免格式错误
            }
        } catch (Exception e) {
            System.out.println("读取 bug.txt 警告: " + e.getMessage());
        }
        return list;//成功返回怪物列表，失败返回空列表
    }

    // 保存玩家存档 (player.txt)
    public static void savePlayer(Programmer p) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("player.txt"))) {
            // 第一行写玩家基础属性: 名字,HP,等级,训练次数,实战经验
            pw.println(p.getName() + "," + p.getHp() + "," + p.getLevel() + "," +
                    p.getTrainCount() + "," + p.isHasBattleExp());

            // 后面每行写一个技能: 技能名,基础伤害,等级,熟练度
            for (Skill s : p.getSkills()) {
                pw.println(s.getName() + "," + s.getBaseDamage() + "," +
                        s.getLevel() + "," + s.getMastery());
            }
            System.out.println("【系统】存档已保存至 player.txt ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取玩家存档 (player.txt)
    public static Programmer loadPlayer() {
        File saveFile = new File("player.txt");
        File templateFile = new File("programmer.txt");
        File targetFile = null;
        boolean isNewGame = false;

        if (saveFile.exists()) {
            targetFile = saveFile;
            System.out.println(">> 检测到存档文件 (player.txt)，正在读取...");
        } else if (templateFile.exists()) {
            targetFile = templateFile;
            System.out.println(">> 未检测到存档，读取初始设定 (programmer.txt)...");
            isNewGame = true;
        } else {
            return null; // 两个文件都没有，让Main去创建新的
        }

        try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            // 1. 读取第一行 (基础属性)
            String line = br.readLine();
            if (line == null) return null;

            String[] baseInfo = line.split(",");
            // 解析: 名字[0], HP[1], Lv[2], Train[3], Battle[4]
            String name = baseInfo[0];
            int hp = Integer.parseInt(baseInfo[1]);
            int lv = Integer.parseInt(baseInfo[2]);
            int train = Integer.parseInt(baseInfo[3]);
            boolean battle = Boolean.parseBoolean(baseInfo[4]);

            // 创建对象
            Programmer player = new Programmer(name, hp);
            player.setLevel(lv);
            player.setTrainCount(train);
            player.setHasBattleExp(battle);

            // 2. 读取后续行 (技能)
            player.clearSkills(); // 先清空构造器自带的默认技能
            while ((line = br.readLine()) != null) {
                String[] skillInfo = line.split(",");
                if (skillInfo.length >= 4) {
                    String sName = skillInfo[0];
                    int sBase = Integer.parseInt(skillInfo[1]);
                    int sLv = Integer.parseInt(skillInfo[2]);
                    int sMastery = Integer.parseInt(skillInfo[3]);

                    // 恢复技能对象
                    player.addSkill(new Skill(sName, sBase));
                }
            }
            return player;
        } catch (Exception e) {
            System.out.println("存档读取出错" + e.getMessage());
            return null;
        }
    }
}