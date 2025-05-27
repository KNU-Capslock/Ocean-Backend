#!/bin/sh
sleep 8
python /app/app/init_db.py
uvicorn app.main:app --host 0.0.0.0 --port 8000