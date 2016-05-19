package jTunes.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/* 50 px high
 * +------------------------------+
 * | = [Albums]                 Q |
 * +------------------------------+
 * 
 */
public class Header extends JPanel {
    private String menuIconPath = "../resources/menuicon.png";
    private String searchIconPath = "../resources/searchicon.png";
    private String title = "[Filter]";
    private MenuPanel menu;
    
    public Header(String title) {
        this(title, 350);
    }
    public Header(String t, int width) {
        title = t;
        
        // set up layout, size, and background color
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, 50));
        setBackground(ColorConstants.MINT_GREEN);
        
        CustomButton menuButton = new CustomButton(ColorConstants.MINT_GREEN,
                                                   menuIconPath,
                                                   "\u2261",
                                                   60);
        menuButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // show slide-in menu
            }
        });
        add(menuButton, BorderLayout.LINE_START);
        
        add(new JLabel(t), BorderLayout.CENTER);
        
        CustomButton searchButton = new CustomButton(ColorConstants.MINT_GREEN,
                                                     searchIconPath,
                                                     "Q",
                                                     30);
        searchButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                
            }
        });
        add(searchButton, BorderLayout.LINE_END);
    }
    
    public void setTitle(String t) {
        title = t;
    }
}
