(
  cd app || exit 1
  npm install
  npm run build
)

cp -rf ./app/dist/. ./src/main/resources/public/