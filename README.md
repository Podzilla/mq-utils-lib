# mq-utils-lib
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
    **❗ Pushing the tag to `main` triggers the automated GitHub Actions release.**

9.  **Verify Automated Release:** Check GitHub "Actions" tab for triggered workflow run. Ensure success. Check "Packages" for published version.

10. **Prepare `dev`:**
    * Switch to `dev`:
      ```bash
      git checkout dev
      ```
    * Merge `main` into `dev`:
      ```bash
      git merge main
      ```
    * Edit `pom.xml` on `dev`: update `<version>` to the **next** SNAPSHOT version (e.g., X.Y.Z+1-SNAPSHOT).
    * Commit version change on `dev`:
      ```bash
      git add pom.xml
      git commit -m "Prepare for next development iteration (X.Y.Z-SNAPSHOT)"
      ```
    * Push `dev`:
      ```bash
      git push origin dev
      ```