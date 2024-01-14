package org.jawbts.thisgame;

import net.minecraft.entity.Entity;import net.minecraft.text.LiteralText;import net.minecraft.text.Text;
import java.util.Objects;import java.util.Random;
public class Thiss { private static final Random rm =                                    new Random();private static String s1, s2, s3;
    String name;Thiss(String name) {
        this.name = name;
    }                                                   static public String generateNewName() {
        String res = generateName();while (res.equals(s1) ||                                                 res.equals(s2) || res.equals(s3)) {res = generateName();
        }s1 = s2;s2 = s3;s3 = res;return res;}static private String generateName() {
        StringBuilder sb = new StringBuilder()                                      ;if (rm.nextBoolean()) {sb.append('t');} else {sb.append('T');
        }if (rm.nextBoolean()) {sb.append('h');}                                                        else {sb.append('H');}if (rm.nextBoolean()) {sb.append('i');
        } else {sb.append('I');}                                                if (rm.nextBoolean()) {sb.append('s');} else {sb.append('S');}return sb.toString();}
    static public Text saySth(Entity entity, String sth) {return new LiteralText('<' + Objects.requireNonNull(entity.getCustomName()).asString()
                + "> " + sth);}static public String getName(Entity entity) {
        return Objects.requireNonNull(entity.getCustomName()).asString();
    }}
