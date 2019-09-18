package com.github.toc;

import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Title:App
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2019/9/18 14:32
 */
public class TOCGenerator {

    private final static JTextArea inputArea = new JTextArea();
    private final static JEditorPane htmlPane1 = new JEditorPane();
    private final static JEditorPane htmlPane2 = new JEditorPane();
    private final static JFrame frame = new JFrame("github-markdown-toc");
    private static final DataHolder OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS, Arrays.asList(
                    CustomExtension.create(),
                    TocExtension.create(),
                    TablesExtension.create(),
                    JekyllTagExtension.create(),
                    SimTocExtension.create()
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
                    String tocText = subContext.getHtmlWriter().toString(0);

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
        frame.setSize(1000, 550);
        frame.setLocation(150, 80);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        inputArea.setLineWrap(true);
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refresh();
            }
        });

        htmlPane1.setEditable(false);
        inputArea.setBackground(new Color(192, 212, 227));

        HTMLEditorKit kit = new HTMLEditorKit();
        htmlPane2.setEditorKit(kit);
        htmlPane2.setEditable(false);

        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        JScrollPane scrollPane1 = new JScrollPane(inputArea);
        JScrollPane scrollPane2 = new JScrollPane(htmlPane1);
        JScrollPane scrollPane3 = new JScrollPane(htmlPane2);

        panel.add(scrollPane1);
        panel.add(scrollPane2);
        panel.add(scrollPane3);
        container.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void refresh() {
        String temp = inputArea.getText();
        if ("".equals(temp)) {
            htmlPane1.setText("");
            htmlPane2.setText("");
        }

        String html = RENDERER.render(PARSER.parse(temp));
        //  System.out.println(html);
        htmlPane2.setText(html);

//        String toc = RENDERER.render(PARSER.parse("[TOC]\n" + temp));
        //  System.out.println(toc);
//        if (html2.indexOf("<div class=\"toc\">") == -1) {
//            htmlPane2.setText("");
//        } else {
//            htmlPane2.setText(html2.substring(0, 6 + html2.indexOf("</div>")));
//        }

//        htmlPane2.setText(RENDERER.render(PARSER.parse(temp)));
    }

    //生成jar包 : mvn clean install -DskipTests
    public static void main(String[] args) {
        new TOCGenerator();

//        String fileSource = System.getProperty("src");
//        if (!TOCUtils.checkSourceFile(fileSource)) {
//            System.out.println("Source file doesn't exists or can't be read. Make sure -Dsrc= param set");
//            return;
//        }
    }
}
