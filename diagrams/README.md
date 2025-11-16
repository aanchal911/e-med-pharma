# UML Diagrams for e-MEDpharma

This directory contains Mermaid diagram files that can be converted to PNG images.

## Available Diagrams

1. **system-architecture.mmd** - System Architecture Flowchart
2. **database-schema.mmd** - Database ER Diagram  
3. **system-flow.mmd** - System Flow Diagram
4. **class-diagram.mmd** - Class Diagram

## Converting to PNG

### Method 1: Using Mermaid CLI
```bash
# Install Mermaid CLI
npm install -g @mermaid-js/mermaid-cli

# Convert to PNG
mmdc -i system-architecture.mmd -o system-architecture.png
mmdc -i database-schema.mmd -o database-schema.png
mmdc -i system-flow.mmd -o system-flow.png
mmdc -i class-diagram.mmd -o class-diagram.png
```

### Method 2: Using Online Tools
1. Visit https://mermaid.live/
2. Copy the content from any .mmd file
3. Paste into the editor
4. Click "Download PNG"

### Method 3: Using VS Code Extension
1. Install "Mermaid Markdown Syntax Highlighting" extension
2. Open any .mmd file
3. Right-click and select "Export Mermaid Diagram"

## Batch Conversion Script

Create a batch file `convert-all.bat`:
```batch
@echo off
mmdc -i system-architecture.mmd -o system-architecture.png
mmdc -i database-schema.mmd -o database-schema.png
mmdc -i system-flow.mmd -o system-flow.png
mmdc -i class-diagram.mmd -o class-diagram.png
echo All diagrams converted to PNG!
```