@echo off
echo Converting Mermaid diagrams to PNG...
echo.

mmdc -i system-architecture.mmd -o system-architecture.png
if %errorlevel% equ 0 (
    echo ✓ System Architecture diagram converted
) else (
    echo ✗ Failed to convert System Architecture diagram
)

mmdc -i database-schema.mmd -o database-schema.png
if %errorlevel% equ 0 (
    echo ✓ Database Schema diagram converted
) else (
    echo ✗ Failed to convert Database Schema diagram
)

mmdc -i system-flow.mmd -o system-flow.png
if %errorlevel% equ 0 (
    echo ✓ System Flow diagram converted
) else (
    echo ✗ Failed to convert System Flow diagram
)

mmdc -i class-diagram.mmd -o class-diagram.png
if %errorlevel% equ 0 (
    echo ✓ Class diagram converted
) else (
    echo ✗ Failed to convert Class diagram
)

echo.
echo All diagrams processed!
pause