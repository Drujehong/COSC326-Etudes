const canvas = document.getElementById('myCanvas');
const ctx = canvas.getContext('2d');

// Array to store information about squares
const squares = [];

const scalingFactor = canvas.height / 2.5;

var count = 0;

var numbOfSquares = 0;

// Function to draw a square
function drawSquare(x, y, size, color) {
    ctx.fillStyle = color;
    ctx.fillRect(x, y, size, size);
}

// Function to draw the first square in the center of the canvas
function drawFirstSquare(x, y, size, color) {
    size = size * scalingFactor;
    const startX = x - size / 2;
    const startY = y - size / 2;
    drawSquare(startX, startY, size, color);
    squares.push({ x: startX, y: startY, size, color });
}

// Function to add subsequent squares with midpoints at the corners of existing squares
function addSquare(scale, color) {
    var size = scale * scalingFactor; // Scale up the size
    const existingSquares = [...squares]; // Create a copy of squares array to avoid adding new squares during loop
    existingSquares.forEach(existingSquare => {
        // Calculate the coordinates of the corners of the existing square
        const topLeft = { x: existingSquare.x, y: existingSquare.y };
        const topRight = { x: existingSquare.x + existingSquare.size, y: existingSquare.y };
        const bottomLeft = { x: existingSquare.x, y: existingSquare.y + existingSquare.size };
        const bottomRight = { x: existingSquare.x + existingSquare.size, y: existingSquare.y + existingSquare.size };

        // Draw squares at the corners of existing squares
        drawSquare(topLeft.x - size / 2, topLeft.y - size / 2, size, color);
        drawSquare(topRight.x - size / 2, topRight.y - size / 2, size, color);
        drawSquare(bottomLeft.x - size / 2, bottomLeft.y - size / 2, size, color);
        drawSquare(bottomRight.x - size / 2, bottomRight.y - size / 2, size, color);

        // Push information about new squares to the squares array
        squares.push({ x: topLeft.x - size / 2, y: topLeft.y - size / 2, size, color });
        squares.push({ x: topRight.x - size / 2, y: topRight.y - size / 2, size, color });
        squares.push({ x: bottomLeft.x - size / 2, y: bottomLeft.y - size / 2, size, color });
        squares.push({ x: bottomRight.x - size / 2, y: bottomRight.y - size / 2, size, color });
    });
}

// Function to clear the canvas and reset the squares array
function clearCanvas() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    squares.length = 0;
    count = 0;
    numbOfSquares = 0;
}

// Function to track number of squares
function removeSquares() {
    for(let i = 0; i < numbOfSquares; i++) {
        squares.shift();
    }
}

// Function to handle form submission
function handleLines() {
    const input = document.getElementById('textInput').value.trim(); // Get the input text
    const lines = input.split('\n'); // Split the input into lines

    lines.forEach(line => {
        const [scale, red, green, blue] = line.split(' ').map(Number); // Parse the line to extract scale and color information
        const color = `rgb(${red}, ${green}, ${blue})`;
        
        if (squares.length === 0) {
            drawFirstSquare(canvas.width / 2, canvas.height / 2, scale, color);
            count++;
            numbOfSquares = count ** 4;
        } else {
            addSquare(scale, color);
            removeSquares();
            numbOfSquares = numbOfSquares * 4;
        }
    });
}

// Add event listener to the clear button
document.getElementById('clearButton').addEventListener('click', clearCanvas);

// Add event listener to the enter button
document.getElementById('enterButton').addEventListener('click', handleLines);
