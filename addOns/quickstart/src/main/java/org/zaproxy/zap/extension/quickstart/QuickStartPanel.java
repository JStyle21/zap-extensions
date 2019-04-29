/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2012 The ZAP development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.quickstart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.ScrollableSizeHint;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.AbstractPanel;
import org.parosproxy.paros.model.OptionsParam;
import org.zaproxy.zap.ZAP;
import org.zaproxy.zap.extension.tab.Tab;
import org.zaproxy.zap.utils.DisplayUtils;
import org.zaproxy.zap.utils.FontUtils;
import org.zaproxy.zap.utils.FontUtils.Size;
import org.zaproxy.zap.view.LayoutHelper;

public class QuickStartPanel extends AbstractPanel implements Tab {

    private static final long serialVersionUID = 1L;

    private ExtensionQuickStart extension;
    private JXPanel panelContent = null;
    private JLabel lowerPadding = new JLabel("");
    private int panelY = 0;
    private AttackPanel attackPanel;
    private JScrollPane jScrollPane;
    private JButton attackButton = null;
    private JPanel buttonPanel;
    private JButton learnMoreButton = null;
    private JButton exploreButton = null;
    private LearnMorePanel learnMorePanel;
    private DefaultExplorePanel defaultExplorePanel;
    private QuickStartSubPanel explorePanel;

    public QuickStartPanel(ExtensionQuickStart extension) {
        super();
        this.extension = extension;
        initialize();
    }

    @SuppressWarnings("deprecation")
    private void initialize() {
        this.setShowByDefault(true);
        this.setIcon(new ImageIcon(ZAP.class.getResource("/resource/icon/16/147.png"))); // 'lightning' icon
        // TODO Use getMenuShortcutKeyMaskEx() (and remove warn suppression) when
        // targeting Java 10+
        this.setDefaultAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | KeyEvent.SHIFT_DOWN_MASK, false));
        this.setMnemonic(Constant.messages.getChar("quickstart.panel.mnemonic"));
        this.setLayout(new BorderLayout());

        panelContent = new JXPanel(new GridBagLayout());
        panelContent.setScrollableHeightHint(ScrollableSizeHint.FIT);

        jScrollPane = new JScrollPane();
        jScrollPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
        jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setViewportView(panelContent);

        this.add(jScrollPane, BorderLayout.CENTER);

        panelContent.setBackground(Color.white);
        panelContent.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        JLabel topTitle = new JLabel(Constant.messages.getString("quickstart.top.panel.title"));
        topTitle.setBackground(Color.WHITE);
        topTitle.setFont(FontUtils.getFont(Size.much_larger));
        topTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelContent.add(topTitle, LayoutHelper.getGBC(0, panelY, 4, 1.0D, 0.0D, GridBagConstraints.BOTH,
                GridBagConstraints.CENTER, new Insets(0, 0, 0, 0)));
        panelContent.add(QuickStartHelper.getWrappedLabel("quickstart.top.panel.message1"),
                LayoutHelper.getGBC(0, ++panelY, 4, 1.0D, new Insets(5, 5, 5, 5)));
        panelContent.add(QuickStartHelper.getWrappedLabel("quickstart.top.panel.message2"),
                LayoutHelper.getGBC(0, ++panelY, 4, 1.0D, new Insets(5, 5, 5, 5)));
        panelContent.add(new JLabel(" "), LayoutHelper.getGBC(0, ++panelY, 4, 1.0D, new Insets(5, 5, 5, 5))); // Spacer

        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(this.getAttackButton());
        buttonPanel.add(this.getExploreButton());
        buttonPanel.add(this.getLearnMoreButton());

        panelContent.add(buttonPanel, LayoutHelper.getGBC(0, ++panelY, 5, 1.0D, 1.0D));

        replacePadding();

        getAttackPanel().setMode(Control.getSingleton().getMode());
        getLearnMorePanel();
    }

    public void backToMainPanel() {
        jScrollPane.setViewportView(panelContent);
    }

    public AttackPanel getAttackPanel() {
        if (attackPanel == null) {
            this.attackPanel = new AttackPanel(this.extension, this);
        }
        return attackPanel;
    }

    private JButton getAttackButton() {
        if (attackButton == null) {
            attackButton = new JButton();
            attackButton.setText(Constant.messages.getString("quickstart.top.button.label.attack"));
            attackButton.setIcon(getAttackPanel().getIcon());
            attackButton.setVerticalTextPosition(AbstractButton.BOTTOM);
            attackButton.setHorizontalTextPosition(AbstractButton.CENTER);
            attackButton.setToolTipText(Constant.messages.getString("quickstart.top.button.tooltip.attack"));
            attackButton.setPreferredSize(DisplayUtils.getScaledDimension(150, 120));

            attackButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jScrollPane.setViewportView(getAttackPanel());
                }
            });
        }
        return attackButton;
    }

    public LearnMorePanel getLearnMorePanel() {
        if (learnMorePanel == null) {
            learnMorePanel = new LearnMorePanel(this.extension, this);
        }
        return learnMorePanel;
    }

    private JButton getLearnMoreButton() {
        if (learnMoreButton == null) {
            learnMoreButton = new JButton();
            learnMoreButton.setText(Constant.messages.getString("quickstart.top.button.label.moreinfo"));
            learnMoreButton.setIcon(getLearnMorePanel().getIcon());
            learnMoreButton.setVerticalTextPosition(AbstractButton.BOTTOM);
            learnMoreButton.setHorizontalTextPosition(AbstractButton.CENTER);
            learnMoreButton.setToolTipText(Constant.messages.getString("quickstart.top.button.tooltip.moreinfo"));
            learnMoreButton.setPreferredSize(DisplayUtils.getScaledDimension(150, 120));

            learnMoreButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jScrollPane.setViewportView(getLearnMorePanel());
                }
            });
        }
        return learnMoreButton;
    }

    public DefaultExplorePanel getDefaultExplorePanel() {
        if (defaultExplorePanel == null) {
            defaultExplorePanel = new DefaultExplorePanel(this.extension, this);
        }
        return defaultExplorePanel;
    }

    private JButton getExploreButton() {
        if (exploreButton == null) {
            exploreButton = new JButton();
            exploreButton.setText(Constant.messages.getString("quickstart.top.button.label.explore"));
            exploreButton.setIcon(getDefaultExplorePanel().getIcon());
            exploreButton.setVerticalTextPosition(AbstractButton.BOTTOM);
            exploreButton.setHorizontalTextPosition(AbstractButton.CENTER);
            exploreButton.setToolTipText(Constant.messages.getString("quickstart.top.button.tooltip.explore"));
            exploreButton.setPreferredSize(DisplayUtils.getScaledDimension(150, 120));

            exploreButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (explorePanel != null) {
                        jScrollPane.setViewportView(explorePanel);
                    } else {
                        jScrollPane.setViewportView(getDefaultExplorePanel());
                    }
                }
            });
        }
        return exploreButton;
    }

    private void replacePadding() {
        if (panelContent != null) {
            // this may or may not be present
            panelContent.remove(this.lowerPadding);
            GridBagConstraints gbc = LayoutHelper.getGBC(0, ++panelY, 4, 1.D, 1.0D);
            panelContent.add(this.lowerPadding, gbc); // Padding at bottom
        }
    }

    protected void setExplorePanel(QuickStartSubPanel panel) {
        this.explorePanel = panel;
    }

    public void addPlugableSpider(PlugableSpider pe) {
        if (this.attackPanel != null) {
            this.attackPanel.addPlugableSpider(pe);
        }
    }

    public void removePlugableSpider(PlugableSpider pe) {
        if (this.attackPanel != null) {
            this.attackPanel.removePlugableSpider(pe);
        }
    }

    public void optionsLoaded(QuickStartParam quickStartParam) {
        this.getAttackPanel().optionsLoaded(quickStartParam);
    }

    public void optionsChanged(OptionsParam optionsParam) {
        this.getDefaultExplorePanel().optionsChanged(optionsParam);
        this.getAttackPanel().optionsChanged(optionsParam);
    }

    public ComboBoxModel<String> getUrlModel() {
        return this.getAttackPanel().getUrlModel();
    }
}
