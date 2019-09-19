package com.github.toc;

import com.vladsch.flexmark.ext.toc.TocBlock;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.toc.internal.TocNodeRenderer;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.*;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.builder.Extension;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Title:TOCGenerator
 * <p>
 * Description:github 不支持 Markdown toc 的解决方案
 * </p>
 *
 * @author Changbao
 * @date 2019/9/18 14:32
 */
public class TOCGenerator {

    private static final String PREFIX_TOC_LOW = "[toc]";
    private static final String PREFIX_TOC_CAP = "[TOC]";
    private final static JTextArea inputArea = new JTextArea();
    private final static JEditorPane htmlPane1 = new JEditorPane();
    private final static JEditorPane htmlPane2 = new JEditorPane();
    private final static JFrame frame = new JFrame("github-markdown-toc");
    private static final DataHolder OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS, Arrays.asList(
                    CustomExtension.create(),
                    TocExtension.create()
            ))
            .set(TocExtension.LEVELS, 255)
            .set(TocExtension.TITLE, "目录")
            .set(TocExtension.DIV_CLASS, "toc");
    private static final Parser PARSER = Parser.builder(OPTIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

    private static class CustomNodeRenderer implements NodeRenderer {
        public static class Factory implements DelegatingNodeRendererFactory {
            @Override
            public NodeRenderer apply(DataHolder options) {
                return new CustomNodeRenderer();
            }

            @Override
            public Set<Class<? extends NodeRendererFactory>> getDelegates() {
                Set<Class<? extends NodeRendererFactory>> set = new HashSet<Class<? extends NodeRendererFactory>>();
                // add node renderer factory classes to which this renderer will delegate some of its rendering
                // core node renderer is assumed to have all depend it so there is no need to add it
                set.add(TocNodeRenderer.Factory.class);
                return set;
                // return null if renderer does not delegate or delegates only to core node renderer
                //return null;
            }
        }

        @Override
        public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
            HashSet<NodeRenderingHandler<?>> set = new HashSet<NodeRenderingHandler<?>>();
            set.add(new NodeRenderingHandler<TocBlock>(TocBlock.class, new com.vladsch.flexmark.html.CustomNodeRenderer<TocBlock>() {
                @Override
                public void render(TocBlock node, NodeRendererContext context, HtmlWriter html) {
                    // test the node to see if it needs overriding
                    NodeRendererContext subContext = context.getDelegatedSubContext(true);
                    subContext.delegateRender();
                    final String tocText = subContext.getHtmlWriter().toString(0);

                    // output to separate stream
                    System.out.println("---- TOC HTML --------------------");
                    System.out.print(tocText);
                    htmlPane1.setText(tocText);
                    System.out.println("----------------------------------\n");

                    html.tagLineIndent("div", () -> html.append(subContext.getHtmlWriter()));
                }
            }));
            return set;
        }
    }

    private static class CustomExtension implements HtmlRenderer.HtmlRendererExtension {

        @Override
        public void rendererOptions(MutableDataHolder mutableDataHolder) {
        }

        @Override
        public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
            rendererBuilder.nodeRendererFactory(new CustomNodeRenderer.Factory());
        }

        static Extension create() {
            return new CustomExtension();
        }
    }

    private TOCGenerator() {
        final int width = 999;
        final int height = 624;
        final Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        //设置背景图
        //返回读取指定资源的输入流 -> 定为资源位置 eg: file:/D:/fastwork/JetBrains/IdeaProject/github-markdown-toc/target/classes/
        //System.out.println(TOCGenerator.class.getResource("/"));
        final ImageIcon img = new ImageIcon(this.getClass().getResource("/image/background.jpg")); //添加图片
        final JLabel background = new JLabel(img);
        frame.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());

        inputArea.setLineWrap(true);
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refresh();
            }
        });
        htmlPane1.setEditable(true);
        HTMLEditorKit kit = new HTMLEditorKit();
        htmlPane2.setEditorKit(kit);
        htmlPane2.setEditable(false);
        inputArea.setOpaque(false);
        htmlPane1.setOpaque(false);
        htmlPane2.setOpaque(false);

        //Title
        final JPanel panelTitle = new JPanel();
        panelTitle.setLayout(new GridLayout(1, 3));
        panelTitle.setBounds(0, 0, width, 30);
        final JLabel label1 = new JLabel("1.输入Markdown文本");
        final JLabel label2 = new JLabel("2.DOM格式目录 👉 替换掉[TOC]");
        final JLabel label3 = new JLabel("3.效果预览");
        label1.setOpaque(false);
        label2.setOpaque(false);
        label3.setOpaque(false);
        panelTitle.add(label1);
        panelTitle.add(label2);
        panelTitle.add(label3);
        panelTitle.setOpaque(false);

        container.add(panelTitle, BorderLayout.PAGE_START);

        //Content
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        JScrollPane scrollPane1 = new JScrollPane(inputArea);
        JScrollPane scrollPane2 = new JScrollPane(htmlPane1);
        JScrollPane scrollPane3 = new JScrollPane(htmlPane2);
        scrollPane1.setOpaque(false);
        scrollPane2.setOpaque(false);
        scrollPane3.setOpaque(false);
        scrollPane1.getViewport().setOpaque(false);
        scrollPane2.getViewport().setOpaque(false);
        scrollPane3.getViewport().setOpaque(false);
        panel.add(scrollPane1);
        panel.add(scrollPane2);
        panel.add(scrollPane3);
        panel.setOpaque(false); //设置为透明 这样就不会遮住后面的背景

        container.add(panel, BorderLayout.CENTER);
        //
        ((JPanel) container).setOpaque(false);
        frame.setResizable(true);
//        frame.setSize(width, height);
        frame.setSize(img.getIconWidth(), img.getIconHeight());
        frame.setLocation(160, 80);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void refresh() {
        final String temp = inputArea.getText();
        if (temp == null || "".equals(temp)) {
            htmlPane1.setText("");
            htmlPane2.setText("");
            return;
        }
        final boolean isLowerTocStart = temp.trim().startsWith(PREFIX_TOC_LOW);
        final boolean isNoTocStart = !isLowerTocStart && !temp.trim().startsWith(PREFIX_TOC_CAP);

        String html = "";
        if (isLowerTocStart) {//[toc] -> [TOC]
            String newTemp = temp.replaceFirst("\\[toc\\]", "");
            html = RENDERER.render(PARSER.parse("[TOC]\n" + newTemp));
        } else if (isNoTocStart) {
            html = RENDERER.render(PARSER.parse("[TOC]\n" + temp));
        } else {
            html = RENDERER.render(PARSER.parse(temp));
        }
        //  System.out.println(html);
        //  if (isNoTocStart) { htmlPane1.setText(""); }
        htmlPane2.setText(html);
    }

    //IDEA Build Artifacts
    //生成jar包 : mvn clean install -DskipTests
    public static void main(String[] args) {
        new TOCGenerator();
    }
}