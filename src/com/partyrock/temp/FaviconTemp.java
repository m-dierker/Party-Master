package com.partyrock.temp;

import java.awt.image.BufferedImage;
import java.io.File;

import net.sf.image4j.codec.ico.ICODecoder;

public class FaviconTemp {
    public static void main(String... args) throws Exception {
        BufferedImage img = ICODecoder.read(new File("Party Rock Icons/RGB Smiley/favicon.ico")).get(0);

        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 16; c++) {

                int ard_r = r;
                int ard_c = c;

                int color = img.getRGB(c, r);

                if (r >= 8) {
                    ard_r -= 8;
                    ard_c += 16;
                }

                int red = ((color >> 16) & 0xFF);
                int green = ((color >> 8) & 0xFF);
                int blue = ((color >> 0) & 0xFF);

                System.out.println("panel[" + ard_r + "][" + ard_c + "][0] = " + red + ";");
                System.out.println("panel[" + ard_r + "][" + ard_c + "][1] = " + green + ";");
                System.out.println("panel[" + ard_r + "][" + ard_c + "][2] = " + blue + ";");
            }
        }
    }
}
