JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Main.java \
	Bicicleta.java \
	Camion.java \
	Autoturism.java \
	Harta.java \
	Motocicleta.java \
	Nod.java \
	Strada.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run: $(CLASSES:.java=.class)
	java Main

build: classes