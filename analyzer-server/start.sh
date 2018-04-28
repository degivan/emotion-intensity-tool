#!/usr/bin/env bash

source env-analyzer/bin/activate env-analyzer
gunicorn --workers 1 --bind 0.0.0.0:8000 analyzer:app