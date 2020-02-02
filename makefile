default:
	@echo "Compiling..."
	@mkdir -p ./out/src
	@cp -r ./src/lib ./out/src
	javac -sourcepath src src/*.java -d out/src
	jar cmf src/manifest.mf out/SnakeGame.jar -C out/src .

run: default
	@echo "Running..."
	java -jar out/SnakeGame.jar

clean:
	-@rm -r out/src/*
	-@rm out/*.jar