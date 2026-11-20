#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

export DISPLAY=:99

if ! pgrep -f '^Xvfb :99 ' >/dev/null; then
    nohup Xvfb :99 -screen 0 1280x800x24 -nolisten tcp \
        > /tmp/banking-display.log 2>&1 &
    sleep 2
fi

if ! pgrep -x openbox >/dev/null; then
    nohup openbox > /tmp/banking-openbox.log 2>&1 &
fi

if ! pgrep -x x11vnc >/dev/null; then
    nohup x11vnc -display :99 -localhost -nopw \
        -forever -shared -rfbport 5900 \
        > /tmp/banking-vnc.log 2>&1 &
fi

if ! pgrep -f '[w]ebsockify.*6080' >/dev/null; then
    nohup websockify --web=/usr/share/novnc/ \
        6080 localhost:5900 > /tmp/banking-browser.log 2>&1 &
fi

mvn compile javafx:run
