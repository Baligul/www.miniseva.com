
# Backup database

```bash
cd ~/repos/www.miniseva.com/backups
./backup.sh 02
```

# Upgrade database

```bash
cd ~/repos/www.miniseva.com/data
./upgrade-3.0.1.sh > upgrade.log 2>&1
grep -i error upgrade.log
rm upgrade.log
```

# Run MiniSeva

```bash
cd ~/repos/www.miniseva.com/
./gradlew bootRun -x test
```