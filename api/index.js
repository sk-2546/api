const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const { spawn } = require('child_process');
const path = require('path');

const app = express();
const port = process.env.PORT || 3000;
const ktorPort = 8080;

// Start Ktor server as a child process
const startKtorServer = () => {
  const isWindows = process.platform === 'win32';
  const gradlewCmd = isWindows ? 'gradlew.bat' : './gradlew';
  
  // Run from project root
  const rootDir = path.resolve(__dirname, '..');
  const proc = spawn(gradlewCmd, [':api:run'], {
    cwd: rootDir,
    stdio: 'pipe'
  });

  proc.stdout.on('data', (data) => {
    console.log(`Ktor stdout: ${data}`);
  });

  proc.stderr.on('data', (data) => {
    console.error(`Ktor stderr: ${data}`);
  });

  return proc;
};

// Create proxy middleware
const ktorProxy = createProxyMiddleware({
  target: `http://localhost:${ktorPort}`,
  changeOrigin: true,
  pathRewrite: {
    '^/api': '' // Remove /api prefix when forwarding to Ktor
  }
});

// Use proxy for all /api routes
app.use('/api', ktorProxy);

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ status: 'ok' });
});

// Start server
if (process.env.NODE_ENV !== 'production') {
  // Only start Ktor in development - Vercel will use the built jar
  const ktorProcess = startKtorServer();
  process.on('SIGTERM', () => {
    ktorProcess.kill();
    process.exit(0);
  });
}

app.listen(port, () => {
  console.log(`Express server listening on port ${port}`);
});