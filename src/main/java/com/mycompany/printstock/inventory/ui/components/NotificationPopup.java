package com.mycompany.printstock.inventory.ui.components;

import com.mycompany.printstock.inventory.model.Barang;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationPopup extends JPopupMenu {
    public NotificationPopup(List<Barang> stokRendah, List<Barang> stokTinggi) {
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);

        GlassPanel panel = new GlassPanel();
        panel.setRadius(16);
        panel.setPreferredSize(new Dimension(320, Math.min(400, 80 + (stokRendah.size() + stokTinggi.size()) * 60)));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Notifikasi");
        title.setFont(new Font("Inter", Font.BOLD, 14));
        title.setForeground(new Color(15, 23, 42));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        if (stokRendah.isEmpty() && stokTinggi.isEmpty()) {
            JLabel empty = new JLabel("Tidak ada notifikasi");
            empty.setFont(new Font("Inter", Font.PLAIN, 13));
            empty.setForeground(new Color(148, 163, 184));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalGlue());
            listPanel.add(empty);
            listPanel.add(Box.createVerticalGlue());
        }

        for (Barang b : stokRendah) {
            listPanel.add(createItem(b, true));
            listPanel.add(Box.createVerticalStrut(8));
        }
        for (Barang b : stokTinggi) {
            listPanel.add(createItem(b, false));
            listPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        add(panel);
    }

    private JPanel createItem(Barang b, boolean isLow) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        item.setMaximumSize(new Dimension(300, 50));

        JLabel icon = new JLabel(LucideIcon.createIcon(
            isLow ? LucideIcon.IconName.ALERT_TRIANGLE : LucideIcon.IconName.CHECK_CIRCLE2, 
            16, 
            isLow ? new Color(217, 119, 6) : new Color(16, 185, 129)
        ));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        JLabel name = new JLabel(b.getNama());
        name.setFont(new Font("Inter", Font.BOLD, 12));
        name.setForeground(new Color(15, 23, 42));

        JLabel status = new JLabel((isLow ? "Stok rendah: " : "Stok melimpah: ") + b.getStokSaatIni() + " " + b.getSatuan());
        status.setFont(new Font("Inter", Font.PLAIN, 11));
        status.setForeground(isLow ? new Color(217, 119, 6) : new Color(16, 185, 129));

        textPanel.add(name);
        textPanel.add(status);

        item.add(icon, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        return item;
    }
}
