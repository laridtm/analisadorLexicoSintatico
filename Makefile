.PHONY: compile run clean package

compile:
	@mkdir -p bin/
	@javac -sourcepath . -d bin ./*.java

clean:
	@rm -rf bin/
	@rm -rf jar/

package: compile
	@mkdir -p jar/
	@cd bin && jar cvfe ../jar/projeto.jar Main ./ . && cd ..

run: compile
	@java -cp bin Main $(FILE)

