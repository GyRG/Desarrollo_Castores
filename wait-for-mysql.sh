#!/bin/bash
# wait-for-mysql.sh

set -e

host="$1"
shift
cmd="$@"

echo "⏳ Esperando a que MySQL esté listo en $host..."

until mysql -h "$host" -u inventario -pinventario123 -e 'SELECT 1' inventario_db; do
  >&2 echo "⏰ MySQL no está listo - esperando..."
  sleep 5
done

>&2 echo "✅ MySQL está listo - ejecutando aplicación"
exec $cmd