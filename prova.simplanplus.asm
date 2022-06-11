-------------------- inizio blocco
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
-------------------- inizio decs
-------------------- dec 1
-------------------- inizio decFun
function_0:
mv $fp $sp
push $ra
-------------------- inizio decFun.block
-------------------- inizio blocco
-------------------- inizio decs
-------------------- inizio stms
-------------------- stm 1
-------------------- IF.cond
lw $al 0($fp)
lw $a0 2($al)
push $a0
li $a0 0
lw $t1 0($sp)
beq $t1 $a0 label_5
li $a0 0
b label_6
label_5:
li $a0 1
label_6:
pop
li $t1 1
beq $a0 $t1 label_4
-------------------- IF.else
-------------------- inizio blocco
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
-------------------- inizio decs
-------------------- dec 1
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
-------------------- inizio stms
-------------------- stm 1
lw $al 0($fp)
lw $al 0($al)
lw $a0 1($al)
lw $a0 0($a0)
push $a0
lw $al 0($fp)
lw $al 0($al)
lw $a0 2($al)
lw $t1 0($sp)
mult $a0 $t1 $a0
pop
push $a0
lw $al 0($fp)
addi $a0 $al -2
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
-------------------- fine stms
label_2:
-------------------- pulizia blocco
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
-------------------- controllo se c'è stato un return
push $a0
li $a0 1
beq $ret $a0 label_7
lw $a0 0($sp)
pop
b label_8
label_7:
lw $a0 0($sp)
pop
-------------------- se $ret == 1 vai a
b label_0
label_8:
-------------------- fine blocco
b label_3
-------------------- IF.then
label_4:
-------------------- inizio blocco
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
-------------------- inizio decs
-------------------- inizio stms
-------------------- stm 1
lw $al 0($fp)
lw $al 0($al)
lw $a0 1($al)
print $a0
-------------------- stm 2
lw $al 0($fp)
lw $al 0($al)
lw $a0 1($al)
del $a0
-------------------- fine stms
label_1:
-------------------- pulizia blocco
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
-------------------- controllo se c'è stato un return
push $a0
li $a0 1
beq $ret $a0 label_9
lw $a0 0($sp)
pop
b label_10
label_9:
lw $a0 0($sp)
pop
-------------------- se $ret == 1 vai a
b label_0
label_10:
-------------------- fine blocco
-------------------- IF.end
label_3:
-------------------- fine stms
label_0:
-------------------- pulizia blocco
addi $sp $sp 0
-------------------- controllo se c'è stato un return
push $a0
li $a0 1
beq $ret $a0 label_11
lw $a0 0($sp)
pop
b label_12
label_11:
lw $a0 0($sp)
pop
-------------------- se $ret == 1 vai a
b function_1
label_12:
-------------------- fine blocco
-------------------- pulizia decFun
function_1:
lw $ra 0($sp)
addi $sp $sp 3
lw $fp 0($sp)
pop
li $ret 0
jr $ra
-------------------- fine decFun
-------------------- dec 2
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
-------------------- inizio stms
-------------------- stm 1
li $a0 1
push $a0
lw $al 0($fp)
addi $a0 $al -3
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
-------------------- stm 2
-------------------- inizio call
push $fp
li $a0 6
push $a0
lw $al 0($fp)
lw $a0 -3($al)
push $a0
mv $al $fp
push $al
jal function_0
-------------------- fine call
-------------------- fine stms
-------------------- pulizia blocco
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
halt
-------------------- fine blocco
