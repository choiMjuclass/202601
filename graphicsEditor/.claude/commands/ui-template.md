Generate a Java Swing UI component template for the graphicsEditor project.

Arguments: $ARGUMENTS

## Instructions

Based on the argument provided, generate a complete Java class template following the project conventions.

**Argument format**: `<ComponentType> [ClassName]`

Supported component types:
- `panel` — JPanel subclass (drawing/display panel)
- `toolbar` — JToolBar subclass
- `menu` — JMenu subclass
- `menubar` — JMenuBar subclass
- `frame` — JFrame subclass
- `dialog` — JDialog subclass
- `button` — custom JButton or AbstractButton subclass

If no ClassName is given, derive a sensible name from the type.

## Project Conventions (MUST follow)

- Class name prefix: `G` + PascalCase (e.g. `GDrawingPanel`, `GShapeToolBar`)
- Enum prefix: `E` + PascalCase; enum values prefix: `e` + PascalCase
- Event handler inner class suffix: `Handler` (e.g. `MouseHandler`)
- Indentation: **tab** (not spaces)
- Package: `org.graphicsEditor`
- No external libraries — pure Java Swing only
- Keep fields private, initialize in constructor
- Inner handler classes implement relevant listener interfaces

## Output

Generate a complete `.java` file. Include:
1. Package declaration
2. Required imports (only what's needed)
3. Class declaration extending the appropriate Swing base
4. Private fields
5. Constructor that sets up the component (layout, look, children)
6. Any necessary inner Handler classes (with TODO stubs for logic)
7. Public getters/setters only if clearly needed

Format the output as a single fenced Java code block.
After the code block, briefly list which files in the project may need to be updated to wire this component in (e.g. `GMainFrame.java`).
