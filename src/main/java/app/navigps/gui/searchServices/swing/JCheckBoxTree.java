package app.navigps.gui.searchServices.swing;

import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author wara
 */
public class JCheckBoxTree extends JTree{
    Vector checkedPaths = new Vector();
    JCheckBoxTreeRenderer checkBoxCellRenderer;

    /**
     *
     * @param value
     */
    public JCheckBoxTree(Object[] value) {
        super(value);
    }

    /**
     *
     * @param value
     */
    public JCheckBoxTree(Vector<?> value){
        super(value);
    }

    /**
     *
     * @param value
     */
    public JCheckBoxTree(Hashtable<?,?> value){
        super(value);
    }

    /**
     *
     * @param root
     */
    public JCheckBoxTree(TreeNode root){
        super(root);
    }

    /**
     *
     * @param root
     * @param asksAllowsChildren
     */
    public JCheckBoxTree(TreeNode root, boolean asksAllowsChildren){
        super(root, asksAllowsChildren);
    }
    /**
     *
     * @param newModel
     */
    public JCheckBoxTree(TreeModel newModel){
        super(newModel);
    }

    /**
     *
     */
    public JCheckBoxTree() {
        super();
        init();
    }

    private void init(){
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        checkBoxCellRenderer= new JCheckBoxTreeRenderer();
        setCellRenderer(checkBoxCellRenderer);
        addMouseListener(new JCheckBoxTreeMouseListener(this));
    }

    /**
     *
     * @param path
     */
    public void setChecked(TreePath path) {
        if(checkedPaths.contains(path)) {
            checkedPaths.remove(path);
            setParentsUnchecked(path);
            setDescendantsUnchecked(path);
        } else {
            checkedPaths.add(path);
            setParentsChecked(path);
            setDescendantsChecked(path);
        }
        setParentsChecked(path);
        repaintPath(path);
    }

    private void setDescendantsChecked(TreePath path) {
        if(!hasBeenExpanded(path)) {
            return;
        }
        Object component = path.getLastPathComponent();
        int childCount = getModel().getChildCount(component);
        for(int i = 0; i < childCount; i++) {
            Object childComponent = getModel().getChild(component, i);
            TreePath childComponentPath = path.pathByAddingChild(childComponent);
            if(!checkedPaths.contains(childComponentPath)) {
                checkedPaths.add(childComponentPath);
                repaintPath(childComponentPath);
            }
            setDescendantsChecked(childComponentPath);
        }
    }

    private void setDescendantsUnchecked(TreePath path) {
        if(hasBeenExpanded(path)) {
            Object cmp = path.getLastPathComponent();

            int component = getModel().getChildCount(cmp);
            for(int i = 0; i < component; i++) {
                Object childComponent = getModel().getChild(cmp, i);
                TreePath childComponentPath = path.pathByAddingChild(childComponent);
                if(checkedPaths.contains(childComponentPath)) {
                    checkedPaths.remove(childComponentPath);
                    repaintPath(childComponentPath);
                }
                setDescendantsUnchecked(childComponentPath);
            }
        }
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean isAnyChildChecked(TreePath path) {
        if ((path != null) && (checkedPaths != null)) {
            for(int i=0; i < checkedPaths.size(); i++) {
                TreePath checkedPath = (TreePath)checkedPaths.elementAt(i);
                if(path.isDescendant(checkedPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParentChecked(TreePath path) {
        if ((path != null) || (checkedPaths != null)){
            if (checkedPaths == null) return false;
            TreePath parentPath = path.getParentPath();
            if(checkedPaths.contains(parentPath)) {
                return true;
            }
        }
        return false;
    }

    private void setParentsChecked(TreePath path) {
        TreePath parentPath = path.getParentPath();
        if(parentPath != null){
            boolean shouldAdd = true;
            Object component = parentPath.getLastPathComponent();
            int childCount = getModel().getChildCount(component);
            for(int i=0; i<childCount;i++) {
                Object childComponent = getModel().getChild(component, i);
                TreePath childPath = parentPath.pathByAddingChild(childComponent);
                if(!checkedPaths.contains(childPath)) {
                    shouldAdd = false;
                }
            }
            if(shouldAdd) {
                checkedPaths.add(parentPath);
            }
            repaintPath(parentPath);
            setParentsChecked(parentPath);
        }
    }

    private void setParentsUnchecked(TreePath path) {
        TreePath parentPath = path.getParentPath();
        if (parentPath != null){
            if(checkedPaths.contains(parentPath)) {
                checkedPaths.remove(parentPath);
            }
            repaintPath(parentPath);
            setParentsUnchecked(parentPath);
        }
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean isChecked(TreePath path) {
        return (checkedPaths.contains(path));
    }

    private void repaintPath(TreePath path) {
        Rectangle pathRect = getPathBounds(path);
        if(pathRect != null){
            repaint(pathRect.x, pathRect.y, pathRect.width, pathRect.height);
        }
    }
}