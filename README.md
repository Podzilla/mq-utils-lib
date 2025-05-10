# mq-utils-lib

## Consuming the `mq-utils-lib` Package

To use the shared `mq-utils-lib` in your microservice project, you need to add it as a dependency in your project's `pom.xml`.

Since this library is hosted on JitPack, you also need to tell Maven where to find it by adding the JitPack repository URL to your `pom.xml`.

---

### 1. **Add the Repository:**

Add the following `<repositories>` section to your microservice's `pom.xml` (if you don't have one, add it under the main `<project>` element; if you do, add this `<repository>` entry inside the existing `<repositories>` section):

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
---

### 2. **Add the Dependency:**

Inside your `<dependencies>` section, add:

```xml
<dependency>
  <groupId>com.github.Podzilla</groupId>
  <artifactId>mq-utils-lib</artifactId>
  <version>v1.1.0</version>
</dependency>
```
---

### 3. **Build Your Project:**

Once added, build your project using:

```bash
mvn clean install -U
```

> 🧠 The `-U` flag forces Maven to **update all snapshots/releases**, ensuring it downloads the latest version from JitPack.

---

## Release Workflow for `mq-utils-lib`

### Branches

* **`dev`**: Ongoing development. Unstable.
* **`main`**: Stable release branch. Only merges for version releases.

---

### Release Workflow (For Admins / Release Managers)

1. **Ensure `dev` is Ready:**

   * All features/fixes completed.
   * CI passes on `dev`.

2. **Pull Latest Branches:**

   ```bash
   git checkout dev && git pull origin dev
   git checkout main && git pull origin main
   ```

3. **Merge `dev` into `main`:**

   ```bash
   git merge dev
   ```

4. **Verify `main` Build:**

   ```bash
   mvn clean verify
   ```

5. **Update `pom.xml` Version:**

   * Change from `*-SNAPSHOT` to `X.Y.Z`.

6. **Commit Version Change:**

   ```bash
   git add pom.xml
   git commit -m "Release X.Y.Z"
   ```

7. **Tag the Release:**

   ```bash
   git tag -a vX.Y.Z -m "Version X.Y.Z Release"
   ```

8. **Push Commits and Tags:**

   ```bash
   git push origin main
   git push origin --tags
   ```

   > ⚠️ Pushing the tag triggers GitHub Actions + JitPack release.

9. **Verify on GitHub:**

   * Check Actions tab for success.
   * Visit: [https://jitpack.io/#Podzilla/mq-utils-lib](https://jitpack.io/#Podzilla/mq-utils-lib)
