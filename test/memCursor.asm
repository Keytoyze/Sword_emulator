j label_0
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
add $zero, $zero, $zero
label_0:
addi $sp, $zero, 16384
sw $zero, 32($zero)
sw $zero, 36($zero)
addi $s1, $zero, 65280
add $s2, $s1, $s1
addi $s3, $zero, 53248
addi $s5, $zero, 32767
addi $t1, $zero, 683
sw $t1, ($s1)
addi $t0, $zero, 2
sw $zero, 4($s1)
lw $t1, ($s1)
add $t1, $t1, $t1
add $t1, $t1, $t1
sw $t1, ($s1)
sw $s5, 4($s1)
lui $t0, 12
addi $t0, $t0, 16132
addi $t5, $zero, 64
label_2:
lui $s4, 65532
label_1:
addi $s4, $s4, 1
bne $s4, $zero, label_1
lw $t1, ($s1)
add $t1, $t1, $t1
add $t1, $t1, $t1
sw $t1, ($s1)
addi $t9, $zero, 1838
sw $t9, ($t0)
addi $t0, $t0, 4
addi $t5, $t5, 65535
bne $t5, $zero, label_2
label_5:
jal label_3
jal label_4
beq $v0, $zero, label_5
add $s0, $zero, $v0
add $a0, $zero, $v0
lui $a1, 12
addi $a1, $a1, 28
addi $a2, $zero, 1792
jal label_6
add $a0, $zero, $s0
jal label_7
addi $a0, $v0, 1792
jal label_8
lw $a0, 60($zero)
lui $a1, 12
addi $a1, $a1, 348
addi $a2, $zero, 1536
jal label_6
addi $a1, $a1, 36
lw $a0, 56($zero)
jal label_6
addi $a1, $a1, 36
lw $a0, 52($zero)
jal label_6
addi $a1, $a1, 36
lw $a0, 48($zero)
jal label_6
add $v0, $zero, $s0
addi $t3, $zero, 28672
add $t3, $t3, $t3
addi $t0, $t3, 116
bne $v0, $t0, label_9
lw $t1, 32($zero)
addi $t1, $t1, 0
sw $t1, 32($zero)
label_9:
addi $t0, $t3, 107
bne $v0, $t0, label_10
lw $t1, 32($zero)
addi $t1, $t1, 65534
sw $t1, 32($zero)
label_10:
addi $t0, $t3, 117
bne $v0, $t0, label_11
lw $t1, 32($zero)
addi $t1, $t1, 65279
sw $t1, 32($zero)
label_11:
addi $t0, $t3, 114
bne $v0, $t0, label_12
lw $t1, 32($zero)
addi $t1, $t1, 256
addi $t1, $t1, 65535
sw $t1, 32($zero)
label_12:
addi $t0, $zero, 90
beq $v0, $t0, label_13
addi $t0, $t3, 90
bne $v0, $t0, label_5
label_13:
jal label_14
lw $t1, 48($zero)
lw $t2, 52($zero)
lui $t0, 17228
addi $t0, $t0, 17746
bne $t1, $t0, label_15
jal label_16
label_15:
lui $t0, 21573
addi $t0, $t0, 21332
bne $t1, $t0, label_17
jal label_18
label_17:
lui $t0, 17490
addi $t0, $t0, 17735
bne $t1, $t0, label_19
jal label_20
label_19:
lui $t0, 17485
addi $t0, $t0, 17741
bne $t1, $t0, label_21
jal label_22
label_21:
sw $zero, 48($zero)
sw $zero, 52($zero)
sw $zero, 56($zero)
sw $zero, 60($zero)
j label_5
label_18:
lw $t1, ($s1)
add $t1, $t1, $t1
add $t1, $t1, $t1
sw $t1, ($s1)
sw $s5, 4($s1)
lui $t9, 65535
addi $sp, $sp, 65532
sw $ra, ($sp)
label_30:
jal label_4
addi $t0, $zero, 21
addi $t1, $zero, 255
and $v0, $v0, $t1
beq $v0, $t0, label_23
beq $v0, $zero, label_24
add $a0, $t8, $zero
lui $a1, 12
addi $a1, $a1, 32
addi $a2, $zero, 1024
jal label_6
addi $a1, $a1, 36
addi $a2, $zero, 1536
jal label_6
add $a0, $zero, $v0
jal label_7
addi $a0, $v0, 1792
jal label_8
lw $a0, 60($zero)
lui $a1, 12
addi $a1, $a1, 348
addi $a2, $zero, 1536
jal label_6
addi $a1, $a1, 36
lw $a0, 56($zero)
jal label_6
addi $a1, $a1, 36
lw $a0, 52($zero)
jal label_6
addi $a1, $a1, 36
lw $a0, 48($zero)
jal label_6
label_24:
addi $a0, $zero, 0
lui $a1, 12
addi $a1, $a1, 1280
jal label_25
addi $a0, $zero, 15616
lui $a1, 12
addi $a1, $a1, 9600
jal label_25
lw $t1, ($s1)
add $t1, $t1, $t1
add $t1, $t1, $t1
sw $t1, ($s1)
lw $t1, ($s1)
lui $t0, 32768
and $t2, $t1, $t0
addi $t9, $t9, 1
beq $t2, $t0, label_26
label_33:
lw $t1, ($s1)
addi $t0, $zero, 24
and $t3, $t1, $t0
beq $t3, $zero, label_27
beq $t3, $t0, label_28
addi $t0, $zero, 8
beq $t3, $t0, label_29
sw $t8, ($s2)
j label_30
label_27:
nor $t0, $zero, $zero
bne $s6, $t0, label_31
nor $s6, $zero, $zero
add $s6, $s6, $s6
label_31:
sw $s6, ($s2)
j label_30
label_28:
lw $t8, 15256($s7)
sw $t8, ($s2)
j label_30
label_29:
lw $t8, 15192($s7)
sw $t8, ($s2)
j label_30
label_23:
lw $ra, ($sp)
addi $sp, $sp, 4
jr $ra
label_26:
lui $t9, 16383
add $s6, $s6, $s6
addi $t0, $zero, 1
or $s6, $s6, $t0
addi $s7, $s7, 4
addi $t0, $zero, 63
and $s7, $s7, $t0
lw $t1, ($s1)
add $t2, $t1, $t1
add $t2, $t2, $t2
sw $t2, ($s1)
sw $s5, 4($s1)
label_32:
lw $t1, ($s1)
lui $t2, 32768
and $t0, $t1, $t2
beq $t0, $t2, label_32
j label_33
label_16:
addi $sp, $sp, 65524
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
lui $t2, 12
addi $t0, $zero, 19200
label_34:
sw $zero, ($t2)
addi $t2, $t2, 4
addi $t0, $t0, 65535
bne $t0, $zero, label_34
addi $t0, $zero, 1
sw $t0, 32($zero)
addi $t0, $zero, 1837
lui $t2, 12
sw $t0, ($t2)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 12
jr $ra
label_4:
addi $sp, $sp, 65516
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
sw $t8, 16($sp)
add $v0, $zero, $zero
add $t8, $zero, $zero
lw $t3, ($s3)
lui $t0, 32768
and $t1, $t3, $t0
bne $t1, $t0, label_35
label_43:
addi $t0, $zero, 255
and $t3, $t3, $t0
and $t2, $t8, $t0
sll $t8, $t8, 8
add $t8, $t8, $t3
sw $t8, ($s2)
addi $t0, $zero, 18
beq $t0, $t3, label_36
addi $t0, $zero, 89
bne $t0, $t3, label_37
label_36:
lw $t0, 64($zero)
lui $t1, 32768
or $t0, $t1, $t0
sw $t0, 64($zero)
label_37:
addi $t0, $zero, 28792
add $t0, $t0, $t0
sll $t1, $t8, 16
srl $t1, $t1, 16
beq $t0, $t1, label_38
addi $t0, $zero, 240
beq $t3, $t0, label_39
lw $t1, 64($zero)
lui $t0, 16384
and $t1, $t1, $t0
beq $t1, $t0, label_40
beq $t3, $t2, label_41
srl $t2, $t8, 16
addi $t0, $zero, 255
and $t2, $t2, $t0
addi $t0, $zero, 224
beq $t0, $t2, label_41
label_42:
lw $t3, ($s3)
lui $t0, 32768
and $t1, $t3, $t0
bne $t1, $t0, label_42
j label_43
label_38:
addi $t3, $zero, 224
label_39:
lw $t2, 64($zero)
lui $t0, 49151
and $t2, $t2, $t0
sw $t2, 64($zero)
j label_44
label_41:
lw $t2, 64($zero)
lui $t0, 16384
or $t2, $t2, $t0
sw $t2, 64($zero)
label_40:
sll $v0, $v0, 8
add $v0, $v0, $t3
lw $t0, 68($zero)
srl $t1, $t0, 24
sll $t0, $t0, 8
add $t0, $t0, $t3
sw $t0, 68($zero)
lw $t0, 72($zero)
sll $t0, $t0, 8
add $t0, $t0, $t1
sw $t0, 72($zero)
addi $t0, $zero, 224
bne $t3, $t0, label_35
label_44:
lw $t3, ($s3)
lui $t0, 32768
and $t1, $t3, $t0
bne $t1, $t0, label_44
addi $t0, $zero, 255
and $t3, $t3, $t0
addi $t0, $zero, 240
beq $t3, $t0, label_44
sll $t8, $t8, 8
add $t8, $t8, $t3
sw $t8, ($s2)
addi $t0, $zero, 30720
add $t0, $t0, $t0
sll $t2, $t8, 16
srl $t2, $t2, 16
addi $t0, $t0, 18
beq $t0, $t2, label_45
addi $t0, $t0, 71
bne $t0, $t2, label_40
label_45:
lw $t0, 64($zero)
lui $t1, 32767
and $t0, $t1, $t0
sw $t0, 64($zero)
j label_40
label_35:
lw $t0, ($sp)
lw $t1, 4($sp)
lw $t2, 8($sp)
lw $t3, 12($sp)
lw $t8, 16($sp)
addi $sp, $sp, 20
jr $ra
label_14:
addi $sp, $sp, 65524
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
lw $t0, 32($zero)
addi $t1, $zero, 127
and $t2, $t0, $t1
addi $t2, $zero, 0
nor $t1, $t1, $t1
and $t0, $t1, $t0
addi $t0, $t0, 256
addi $t1, $zero, 15360
bne $t0, $t1, label_46
addi $t0, $zero, 0
addi $sp, $sp, 65532
sw $ra, ($sp)
jal label_16
lw $ra, ($sp)
addi $sp, $sp, 4
label_46:
add $t0, $t0, $t2
sw $t0, 32($zero)
addi $sp, $sp, 65532
sw $ra, ($sp)
addi $a0, $zero, 1837
jal label_8
lw $ra, ($sp)
addi $sp, $sp, 4
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 12
jr $ra
label_6:
addi $sp, $sp, 65516
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
sw $t4, 16($sp)
addi $t4, $zero, 8
addi $t3, $a1, 28
label_48:
addi $t2, $zero, 15
and $t0, $a0, $t2
addi $t1, $zero, 9
slt $t2, $t1, $t0
addi $t0, $t0, 48
beq $t2, $zero, label_47
addi $t0, $t0, 7
label_47:
add $t0, $t0, $a2
sw $t0, ($t3)
srl $a0, $a0, 4
addi $t3, $t3, 65532
addi $t4, $t4, 65535
bne $t4, $zero, label_48
lw $t4, 16($sp)
lw $t3, 12($sp)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 20
jr $ra
label_20:
addi $sp, $sp, 65508
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
sw $t4, 16($sp)
sw $t5, 20($sp)
sw $ra, 24($sp)
addi $sp, $sp, 65408
sw $zero, ($sp)
sw $at, 4($sp)
sw $v0, 8($sp)
sw $v1, 12($sp)
addi $v1, $zero, 28
lui $at, 1
addi $at, $at, 4
jal label_49
label_49:
sw $a0, 16($sp)
lw $v0, ($ra)
add $v0, $v0, $at
sw $v0, ($ra)
addi $v1, $v1, 65535
bne $v1, $zero, label_49
lui $v0, 44964
addi $v0, $v0, 16
sw $v0, ($ra)
add $t3, $a1, $zero
addi $t0, $zero, 4
addi $t1, $zero, 8
addi $t2, $zero, 0
label_63:
bne $t2, $zero, label_50
addi $t4, $zero, 1106
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $zero, 1072
j label_51
label_50:
addi $t4, $zero, 1
bne $t2, $t4, label_52
addi $t4, $zero, 1121
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $zero, 1140
j label_51
label_52:
slti $t4, $t2, 4
beq $t4, $zero, label_53
addi $t4, $zero, 1142
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $t2, 65534
addi $t4, $t4, 1072
j label_51
label_53:
slti $t4, $t2, 8
beq $t4, $zero, label_54
addi $t4, $zero, 1121
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $t2, 65532
addi $t4, $t4, 1072
j label_51
label_54:
slti $t4, $t2, 16
beq $t4, $zero, label_55
addi $t4, $zero, 1140
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $t2, 65528
addi $t4, $t4, 1072
j label_51
label_55:
slti $t4, $t2, 24
beq $t4, $zero, label_56
addi $t4, $zero, 1139
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $t2, 65520
addi $t4, $t4, 1072
j label_51
label_56:
slti $t4, $t2, 26
beq $t4, $zero, label_57
addi $t4, $zero, 1140
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $t2, 65520
addi $t4, $t4, 1072
j label_51
label_57:
slti $t4, $t2, 28
beq $t4, $zero, label_58
addi $t4, $zero, 1131
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $t2, 65510
addi $t4, $t4, 1072
j label_51
label_58:
addi $t4, $zero, 28
bne $t2, $t4, label_59
addi $t4, $zero, 1127
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $zero, 1136
j label_51
label_59:
addi $t4, $zero, 29
bne $t2, $t4, label_60
addi $t4, $zero, 1139
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $zero, 1136
j label_51
label_60:
addi $t4, $zero, 30
bne $t2, $t4, label_61
addi $t4, $zero, 1126
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $zero, 1136
j label_51
label_61:
addi $t4, $zero, 1138
sw $t4, ($a1)
addi $a1, $a1, 4
addi $t4, $zero, 1121
label_51:
sw $t4, ($a1)
addi $a1, $a1, 4
jal label_62
label_62:
lw $a0, ($sp)
add $t5, $zero, $ra
addi $a2, $zero, 1792
jal label_6
add $ra, $zero, $t5
addi $a1, $a1, 32
lw $v0, ($t5)
addi $v0, $v0, 4
sw $v0, ($t5)
addi $t2, $t2, 1
addi $t1, $t1, 65535
bne $t1, $zero, label_63
addi $t1, $zero, 8
addi $t3, $t3, 320
add $a1, $t3, $zero
addi $t0, $t0, 65535
bne $t0, $zero, label_63
lui $v0, 36772
sw $v0, ($t5)
lw $zero, ($sp)
lw $at, 4($sp)
lw $v0, 8($sp)
lw $v1, 12($sp)
addi $sp, $sp, 128
lw $ra, 24($sp)
lw $t5, 20($sp)
lw $t4, 16($sp)
lw $t3, 12($sp)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 28
jr $ra
label_25:
addi $sp, $sp, 65512
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
sw $t4, 16($sp)
sw $ra, 20($sp)
add $t2, $a0, $zero
add $t3, $a1, $zero
add $a1, $t3, $zero
addi $t0, $zero, 24
addi $t1, $zero, 8
addi $a2, $zero, 1536
jal label_6
addi $a1, $a1, 32
sw $zero, ($a1)
addi $a1, $a1, 4
label_64:
lw $a0, ($t2)
addi $a2, $zero, 1792
jal label_6
addi $t2, $t2, 4
addi $a1, $a1, 32
sw $zero, ($a1)
addi $a1, $a1, 4
addi $t1, $t1, 65535
bne $t1, $zero, label_64
addi $t3, $t3, 320
add $a0, $zero, $t2
add $a1, $t3, $zero
addi $a2, $zero, 1536
jal label_6
addi $a1, $a1, 32
sw $zero, ($a1)
addi $a1, $a1, 4
addi $t1, $zero, 8
addi $t0, $t0, 65535
bne $t0, $zero, label_64
lw $ra, 20($sp)
lw $t4, 16($sp)
lw $t3, 12($sp)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 24
jr $ra
label_22:
addi $sp, $sp, 65512
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
sw $t4, 16($sp)
sw $ra, 20($sp)
addi $t0, $zero, 56
add $t3, $zero, $zero
addi $t4, $zero, 2
label_66:
lw $t1, ($t0)
srl $a0, $t1, 24
jal label_65
addi $t2, $zero, 15
and $v0, $v0, $t2
sll $t3, $t3, 4
add $t3, $t3, $v0
sll $a0, $t1, 8
srl $a0, $a0, 24
jal label_65
addi $t2, $zero, 15
and $v0, $v0, $t2
sll $t3, $t3, 4
add $t3, $t3, $v0
sll $a0, $t1, 16
srl $a0, $a0, 24
jal label_65
addi $t2, $zero, 15
and $v0, $v0, $t2
sll $t3, $t3, 4
add $t3, $t3, $v0
sll $a0, $t1, 24
srl $a0, $a0, 24
jal label_65
addi $t2, $zero, 15
and $v0, $v0, $t2
sll $t3, $t3, 4
add $t3, $t3, $v0
addi $t0, $t0, 65532
addi $t4, $t4, 65535
bne $t4, $zero, label_66
add $a0, $zero, $t3
lui $a1, 12
addi $a1, $a1, 1280
jal label_25
lw $ra, 20($sp)
lw $t4, 16($sp)
lw $t3, 12($sp)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 24
jr $ra
label_3:
addi $sp, $sp, 65520
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
lw $t2, 32($zero)
addi $t0, $zero, 127
and $t3, $t2, $t0
add $t3, $t3, $t3
addi $t0, $zero, 16128
and $t2, $t2, $t0
add $t2, $t2, $t3
sll $t2, $t2, 17
lw $t1, ($s1)
addi $t0, $zero, 1023
and $t1, $t1, $t0
add $t1, $t1, $t1
add $t1, $t1, $t1
or $t1, $t1, $t2
sw $t1, ($s1)
lw $t3, 12($sp)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 16
jr $ra
label_7:
addi $sp, $sp, 65520
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
addi $t0, $zero, 28672
add $t0, $t0, $t0
and $t1, $a0, $t0
bne $t1, $t0, label_67
lui $t1, 65535
nor $t1, $t1, $t1
and $t1, $t1, $a0
addi $t2, $t0, 117
bne $t2, $t1, label_68
addi $v0, $zero, 30
j label_69
label_68:
addi $t2, $t0, 114
bne $t2, $t1, label_70
addi $v0, $zero, 31
j label_69
label_70:
addi $t2, $t0, 107
bne $t2, $t1, label_71
addi $v0, $zero, 29
j label_69
label_71:
addi $t2, $t0, 116
bne $t2, $t1, label_72
addi $v0, $zero, 28
j label_69
label_72:
addi $t2, $t0, 108
bne $t2, $t1, label_73
addi $v0, $zero, 11
j label_69
label_73:
addi $t2, $t0, 90
bne $t2, $t1, label_74
addi $v0, $zero, 13
j label_69
label_74:
add $zero, $zero, $zero
label_67:
addi $t0, $zero, 252
and $t1, $t0, $a0
lw $t1, 15360($t1)
addi $t0, $zero, 3
and $t2, $t0, $a0
beq $t2, $t0, label_75
label_76:
srl $t1, $t1, 8
addi $t0, $t0, 65535
bne $t2, $t0, label_76
label_75:
addi $t0, $zero, 255
and $v0, $t0, $t1
lw $t0, 64($zero)
lui $t1, 32768
and $t0, $t1, $t0
bne $t0, $t1, label_77
addi $v0, $v0, 32
label_77:
addi $t0, $zero, 13
beq $t0, $v0, label_69
lw $t0, 48($zero)
srl $t1, $t0, 24
sll $t0, $t0, 8
add $t0, $t0, $v0
sw $t0, 48($zero)
lw $t0, 52($zero)
srl $t2, $t0, 24
sll $t0, $t0, 8
add $t0, $t0, $t1
sw $t0, 52($zero)
lw $t0, 56($zero)
srl $t1, $t0, 24
sll $t0, $t0, 8
add $t0, $t0, $t2
sw $t0, 56($zero)
lw $t0, 60($zero)
sll $t0, $t0, 8
add $t0, $t0, $t1
sw $t0, 60($zero)
label_69:
lw $t3, 12($sp)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 16
jr $ra
label_65:
addi $sp, $sp, 65516
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
sw $t3, 12($sp)
sw $ra, 16($sp)
add $t2, $zero, $a0
jal label_78
lui $t0, 32768
and $t1, $v0, $t0
beq $t0, $t1, label_79
add $a0, $zero, $t2
jal label_80
label_79:
lw $ra, 16($sp)
lw $t3, 12($sp)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 20
jr $ra
label_78:
addi $sp, $sp, 65524
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
addi $v0, $a0, 65488
addi $t0, $zero, 57
slt $t1, $t0, $a0
bne $t1, $zero, label_81
addi $t0, $zero, 48
slt $t1, $a0, $t0
bne $t1, $zero, label_81
lui $t0, 32768
or $v0, $v0, $t0
label_81:
lw $t0, ($sp)
lw $t1, 4($sp)
lw $t2, 8($sp)
addi $sp, $sp, 12
jr $ra
label_80:
addi $sp, $sp, 65524
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
addi $v0, $a0, 65481
addi $t0, $zero, 70
slt $t1, $t0, $a0
bne $t1, $zero, label_82
addi $t0, $zero, 65
slt $t1, $a0, $t0
bne $t1, $zero, label_82
lui $t0, 32768
or $v0, $v0, $t0
label_82:
lw $t0, ($sp)
lw $t1, 4($sp)
lw $t2, 8($sp)
addi $sp, $sp, 12
jr $ra
label_8:
addi $sp, $sp, 65524
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
lw $t0, 32($zero)
addi $t1, $zero, 16128
and $t1, $t0, $t1
srl $t2, $t1, 1
srl $t2, $t2, 1
add $t2, $t2, $t1
addi $t1, $zero, 127
and $t1, $t0, $t1
add $t1, $t1, $t1
add $t1, $t1, $t1
add $t2, $t2, $t1
lui $t1, 12
add $t2, $t1, $t2
add $t1, $zero, $a0
sw $t1, ($t2)
addi $sp, $sp, 65532
sw $ra, ($sp)
jal label_83
lw $ra, ($sp)
addi $sp, $sp, 4
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 12
jr $ra
label_83:
addi $sp, $sp, 65524
sw $t0, ($sp)
sw $t1, 4($sp)
sw $t2, 8($sp)
lw $t0, 32($zero)
addi $t1, $zero, 127
and $t2, $t0, $t1
addi $t2, $t2, 1
nor $t1, $t1, $t1
and $t0, $t1, $t0
addi $t1, $zero, 80
bne $t2, $t1, label_84
add $t2, $zero, $zero
addi $t0, $t0, 256
addi $t1, $zero, 15360
bne $t0, $t1, label_84
addi $t0, $zero, 0
addi $sp, $sp, 65532
sw $ra, ($sp)
jal label_16
lw $ra, ($sp)
addi $sp, $sp, 4
label_84:
add $t0, $t0, $t2
sw $t0, 32($zero)
lw $t2, 8($sp)
lw $t1, 4($sp)
lw $t0, ($sp)
addi $sp, $sp, 12
jr $ra
addi $sp, $sp, 65500
sw $s0, ($sp)
sw $s1, 4($sp)
sw $s2, 8($sp)
sw $s3, 12($sp)
sw $s4, 16($sp)
sw $s5, 20($sp)
sw $s6, 24($sp)
sw $s7, 28($sp)
sw $ra, 32($sp)
jr $ra
lw $s0, ($sp)
lw $s1, 4($sp)
lw $s2, 8($sp)
lw $s3, 12($sp)
lw $s4, 16($sp)
lw $s5, 20($sp)
lw $s6, 24($sp)
lw $s7, 28($sp)
lw $ra, 32($sp)
addi $sp, $sp, 36
jr $ra
jr $ra
.space 3280
label_87:
.space 8220
.word 0xFFFFFF00
sltu $zero, $zero, $zero
lb $zero, ($zero)
.word 0x3F
sll $zero, $t4, 0
.word 0xFFFF0000
.word 0xFFFF
.word 0xFFFFD000
.word 0x0
beq $t0, $s1, label_85
addi $v0, $s1, 8738
andi $s3, $t9, 13107
.word 0x44444444
bnel $t2, $s5, label_86
.word 0x66666666
.word 0x77777777
lwl $t0, 34952($a0)
lwr $t9, 39321($t4)
swl $t2, 43690($s5)
swr $k1, 48059($sp)
pref $t4, 52428($a2)
.word 0xDDDDDDDD
swc3 $t6, 61166($s7)
.word 0xFFFFFFFF
bnel $t3, $fp, label_87
ldc1 $sp, 64473($sp)
ldc1 $k1, 64953($fp)
.word 0xDFCFFCFB
.word 0xDFCFBFFF
sdc1 $s3, 57343($ra)
.word 0xFFFFDF3D
.word 0xFFFF9DB9
.word 0xFFFFBCFB
.word 0xDFCFFCFB
.word 0xDFCFBFFF
ldc1 $k1, 40959($fp)
ldc1 $k1, 64953($fp)
ldc1 $sp, 64473($sp)
.word 0xFFFF07E0
.word 0x7E0FFF
add $fp, $sp, $sp
add $ra, $fp, $fp
j label_88
.space 28
.word 0xF9FFF5
.word 0xF3F1F2FC
.word 0xFFFAF8F6
sdc1 $t1, 24831($zero)
.word 0xFF0000FF
.word 0x5131FF
.word 0xFFFF5A53
.word 0x41573200
.word 0xFF435844
.word 0x45343300
.word 0xFF205646
bnel $v0, $s2, label_89
.word 0xFF4E4248
.word 0x475936FF
.word 0xFFFF4D4A
bnel $t1, $s7, label_90
.word 0xFF2C4B49
.word 0x4F3039FF
.word 0xFF2E2F4C
xori $s0, $k0, 11775
.word 0xFFFF27FF
.word 0x5B3DFFFF
.word 0xD5D
.word 0xFF5CFFFF
.word 0xFFFFFFFF
.word 0xFFFF08FF
.word 0xFF31FF34
ori $ra, $ra, 65535
andi $t6, $at, 12853
ori $t8, $s1, 7167
.word 0xF12B332D
slti $t9, $s1, 255
.space 32
xori $s3, $v1, 49152
.word 0xF9FFF5
.word 0xF3F1F2FC
.word 0xFFFAF8F6
sdc1 $t1, 24831($zero)
.word 0xFF0000FF
.word 0x7131FF
.word 0xFFFF7A73
.word 0x61773200
.word 0xFF637864
.word 0x65343300
.word 0xFF207666
.word 0x74723500
.word 0xFF6E6268
.word 0x677936FF
.word 0xFFFF6D6A
.word 0x753738FF
.word 0xFF2C6B69
.word 0x6F3039FF
.word 0xFF2E2F6C
xori $s0, $k1, 11775
.word 0xFFFF27FF
.word 0x5B3DFFFF
.word 0xD5D
.word 0xFF5CFFFF
.word 0xFFFFFFFF
.word 0xFFFF08FF
.word 0xFF31FF34
ori $ra, $ra, 65535
andi $t6, $at, 12853
ori $t8, $s1, 7167
.word 0xF12B332D
slti $t9, $s1, 255
.space 32
