
#cleanup some administrative and bookeeping files that are not really needed.
rm README.md
rm LICENSE

#copy the gradle templates into the config/gradle dir
mkdir -p config/gradle

ROOT_PROJECT_DIR=`pwd`

cd $ROOT_PROJECT_DIR/config/gradle

curl https://codeload.github.com/patrickianwilson/gradle-templates/zip/master > temp.zip
unzip temp.zip
mv gradle-templates-master/* .
rm -r gradle-templates-master
rm temp.zip



cd $ROOT_PROJECT_DIR

