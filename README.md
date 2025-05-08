# mq-utils-lib

## Consuming the `mq-utils-lib` Package

To use the shared `mq-utils-lib` in your microservice project, you need to add it as a dependency in your project's `pom.xml`.


Since this library is hosted on GitHub Packages, you also need to tell Maven where to find it by adding the GitHub Packages repository URL to your `pom.xml`.

1.  **Add the Repository:**
    Add the following `<repositories>` section to your microservice's `pom.xml` (if you don't have one, add it under the main `<project>` element; if you do, add this `<repository>` entry inside the existing `<repositories>` section):

    ```xml
    <repositories>
        <repository>
            <id>github-podzilla</id> <name>GitHub Podzilla Maven Packages</name>
            <url>[https://maven.pkg.github.com/Podzilla](https://maven.pkg.github.com/Podzilla)</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled> </snapshots>
        </repository>
        </repositories>
    ```
    * **Note:** If the `mq-utils-lib` repository is **public**, this is all you need for the repository configuration.
    * **If the `mq-utils-lib` repository is private**, ensure your Maven `settings.xml` (usually `~/.m2/settings.xml`) has a `<server>` entry with the `<id>` `github-podzilla` and your GitHub credentials (username and PAT).

2.  **Add the Dependency:**
    Add the following `<dependency>` block within the `<dependencies>` section of your microservice's `pom.xml`:

    ```xml
    <dependencies>
        ...

        <dependency>
            <groupId>com.podzilla</groupId> <artifactId>mq-utils-lib</artifactId> <version>1.0.0</version> </dependency>

        ...
    </dependencies>
    ```
    * Use the specific release version (e.g., `1.0.0`) or SNAPSHOT version (e.g., `1.0.1-SNAPSHOT`) you want to use.

3.  **Build Your Project:**
    After adding these sections, run a standard Maven command like `mvn clean install` or `mvn package` in your microservice's directory. Maven will download the `mq-utils-lib` JAR from GitHub Packages.


## Release Workflow for `mq-utils-lib`


## Branches

* **`dev`**: Ongoing development branch. Changes merge here first. **Not stable.**
* **`main`**: Stable, released versions branch. Merges here only for releases. Commits match release versions.

## Release Workflow (For Admins / Release Managers)

Follow these steps to publish a new release:

1.  **Ensure `dev` Ready:** Verify all release features/fixes are in `dev`. Latest CI on `dev` must pass.

2.  **Pull Latest:** Sync local `dev` and `main`.
    ```bash
    git checkout dev
    git pull origin dev
    git checkout main
    git pull origin main
    ```

3.  **Merge `dev` into `main`:** Merge `dev` history into `main`.
    ```bash
    git merge dev
    ```
    *(Resolve conflicts if needed)*

4.  **Verify `main` Build:** Build and test merged `main` locally.
    ```bash
    mvn clean verify
    ```
    Ensure success.

5.  **Update Release Version:** Determine Semantic Version (X.Y.Z). Edit `pom.xml` on `main`: change `<version>` from SNAPSHOT to X.Y.Z.

6.  **Commit Version Change:** Commit the `pom.xml` update on `main`.
    ```bash
    git add pom.xml
    git commit -m "Release X.Y.Z"
    ```

7.  **Tag Release Commit:** Create annotated Git tag `vX.Y.Z` on the version commit on `main`.
    ```bash
    git tag -a vX.Y.Z -m "Version X.Y.Z Release"
    ```

8.  **Push Commits and Tags:** Push `main` commits and the release tag to remote.
    ```bash
    git push origin main
    git push origin --tags
    ```
    **❗❗❗❗ Pushing the tag to `main` triggers the automated GitHub Actions release.**

9.  **Verify Automated Release:** Check GitHub "Actions" tab for triggered workflow run. Ensure success. Check "Packages" for published version.
