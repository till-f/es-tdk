
   .section .trap_table
	.global	_trap_table
	.type	_trap_table, @function
_trap_table:

   .global IVOR0trap
   .align 4
   IVOR0trap:    b  IVOR0trap
   .global IVOR1trap
   .align 4
   IVOR1trap:    b  IVOR1trap
   .global IVOR2trap
   .align 4
   IVOR2trap:    b  IVOR2trap
   .global IVOR3trap
   .align 4
   IVOR3trap:    b  IVOR3trap
   .global IVOR4trap
   .align 4
   IVOR4trap:    b  IVOR4trap
   .global IVOR5trap
   .align 4
   IVOR5trap:    b  IVOR5trap
   .global IVOR6trap
   .align 4
   IVOR6trap:    b  IVOR6trap
   .global IVOR7trap
   .align 4
   IVOR7trap:    b  IVOR7trap
   .global IVOR8trap
   .align 4
   IVOR8trap:    b  IVOR8trap
   .global IVOR9trap
   .align 4
   IVOR9trap:    b  IVOR9trap
   .global IVOR10trap
   .align 4
   IVOR10trap:    b  IVOR10trap
   .global IVOR11trap
   .align 4
   IVOR11trap:    b  IVOR11trap
   .global IVOR12trap
   .align 4
   IVOR12trap:    b  IVOR12trap
   .global IVOR13trap
   .align 4
   IVOR13trap:    b  IVOR13trap
   .global IVOR14trap
   .align 4
   IVOR14trap:    b  IVOR14trap
   .global IVOR15trap
   .align 4
   IVOR15trap:    b  IVOR15trap
   .global IVOR32trap
   .align 4
   IVOR32trap:    b  IVOR32trap
   .global IVOR33trap
   .align 4
   IVOR33trap:    b  IVOR33trap
   .global IVOR34trap
   .align 4
   IVOR34trap:    b  IVOR34trap

.size	_trap_table, .-_trap_table


   .section .interrupt_table
	.global	_interrupt_table
	.type	_interrupt_table, @function
_interrupt_table:

    .align 4
    hw_vect0:      b  hw_vect0
    .align 4
    hw_vect1:      b  hw_vect1
    .align 4
    hw_vect2:      b  hw_vect2
    .align 4
    hw_vect3:      b  SwIrq3Handler
    .align 4
    hw_vect4:      b  hw_vect4
    .align 4
    hw_vect5:      b  hw_vect5
    .align 4
    hw_vect6:      b  hw_vect6
    .align 4
    hw_vect7:      b  hw_vect7
    .align 4
    hw_vect8:      b  hw_vect8
    .align 4
    hw_vect9:      b  hw_vect9
    .align 4
    hw_vect10:     b  hw_vect10
    .align 4
    hw_vect11:     b  hw_vect11
    .align 4
    hw_vect12:     b  hw_vect12
    .align 4
    hw_vect13:     b  hw_vect13
    .align 4
    hw_vect14:     b  hw_vect14
    .align 4
    hw_vect15:     b  hw_vect15
    .align 4
    hw_vect16:     b  hw_vect16
    .align 4
    hw_vect17:     b  hw_vect17
    .align 4
    hw_vect18:     b  hw_vect18
    .align 4
    hw_vect19:     b  hw_vect19
    .align 4
    hw_vect20:     b  hw_vect20
    .align 4
    hw_vect21:     b  hw_vect21
    .align 4
    hw_vect22:     b  hw_vect22
    .align 4
    hw_vect23:     b  hw_vect23
    .align 4
    hw_vect24:     b  hw_vect24
    .align 4
    hw_vect25:     b  hw_vect25
    .align 4
    hw_vect26:     b  hw_vect26
    .align 4
    hw_vect27:     b  hw_vect27
    .align 4
    hw_vect28:     b  hw_vect28
    .align 4
    hw_vect29:     b  hw_vect29
    .align 4
    hw_vect30:     b  hw_vect30
    .align 4
    hw_vect31:     b  hw_vect31
    .align 4
    hw_vect32:     b  hw_vect32
    .align 4
    hw_vect33:     b  hw_vect33
    .align 4
    hw_vect34:     b  hw_vect34
    .align 4
    hw_vect35:     b  hw_vect35
    .align 4
    hw_vect36:     b  hw_vect36
    .align 4
    hw_vect37:     b  hw_vect37
    .align 4
    hw_vect38:     b  hw_vect38
    .align 4
    hw_vect39:     b  hw_vect39
    .align 4
    hw_vect40:     b  hw_vect40
    .align 4
    hw_vect41:     b  hw_vect41
    .align 4
    hw_vect42:     b  hw_vect42
    .align 4
    hw_vect43:     b  hw_vect43
    .align 4
    hw_vect44:     b  hw_vect44
    .align 4
    hw_vect45:     b  hw_vect45
    .align 4
    hw_vect46:     b  hw_vect46
    .align 4
    hw_vect47:     b  hw_vect47
    .align 4
    hw_vect48:     b  hw_vect48
    .align 4
    hw_vect49:     b  hw_vect49
    .align 4
    hw_vect50:     b  hw_vect50
    .align 4
    hw_vect51:     b  hw_vect51
    .align 4
    hw_vect52:     b  hw_vect52
    .align 4
    hw_vect53:     b  hw_vect53
    .align 4
    hw_vect54:     b  hw_vect54
    .align 4
    hw_vect55:     b  hw_vect55
    .align 4
    hw_vect56:     b  hw_vect56
    .align 4
    hw_vect57:     b  hw_vect57
    .align 4
    hw_vect58:     b  hw_vect58
    .align 4
    hw_vect59:     b  hw_vect59
    .align 4
    hw_vect60:     b  hw_vect60
    .align 4
    hw_vect61:     b  hw_vect61
    .align 4
    hw_vect62:     b  hw_vect62
    .align 4
    hw_vect63:     b  hw_vect63
    .align 4
    hw_vect64:     b  hw_vect64
    .align 4
    hw_vect65:     b  hw_vect65
    .align 4
    hw_vect66:     b  hw_vect66
    .align 4
    hw_vect67:     b  hw_vect67
    .align 4
    hw_vect68:     b  hw_vect68
    .align 4
    hw_vect69:     b  hw_vect69
    .align 4
    hw_vect70:     b  hw_vect70
    .align 4
    hw_vect71:     b  hw_vect71
    .align 4
    hw_vect72:     b  hw_vect72
    .align 4
    hw_vect73:     b  hw_vect73
    .align 4
    hw_vect74:     b  hw_vect74
    .align 4
    hw_vect75:     b  hw_vect75
    .align 4
    hw_vect76:     b  hw_vect76
    .align 4
    hw_vect77:     b  hw_vect77
    .align 4
    hw_vect78:     b  hw_vect78
    .align 4
    hw_vect79:     b  hw_vect79
    .align 4
    hw_vect80:     b  hw_vect80
    .align 4
    hw_vect81:     b  hw_vect81
    .align 4
    hw_vect82:     b  hw_vect82
    .align 4
    hw_vect83:     b  hw_vect83
    .align 4
    hw_vect84:     b  hw_vect84
    .align 4
    hw_vect85:     b  hw_vect85
    .align 4
    hw_vect86:     b  hw_vect86
    .align 4
    hw_vect87:     b  hw_vect87
    .align 4
    hw_vect88:     b  hw_vect88
    .align 4
    hw_vect89:     b  hw_vect89
    .align 4
    hw_vect90:     b  hw_vect90
    .align 4
    hw_vect91:     b  hw_vect91
    .align 4
    hw_vect92:     b  hw_vect92
    .align 4
    hw_vect93:     b  hw_vect93
    .align 4
    hw_vect94:     b  hw_vect94
    .align 4
    hw_vect95:     b  hw_vect95
    .align 4
    hw_vect96:     b  hw_vect96
    .align 4
    hw_vect97:     b  hw_vect97
    .align 4
    hw_vect98:     b  hw_vect98
    .align 4
    hw_vect99:     b  hw_vect99
    .align 4
    hw_vect100:    b  hw_vect100
    .align 4
    hw_vect101:    b  hw_vect101
    .align 4
    hw_vect102:    b  hw_vect102
    .align 4
    hw_vect103:    b  hw_vect103
    .align 4
    hw_vect104:    b  hw_vect104
    .align 4
    hw_vect105:    b  hw_vect105
    .align 4
    hw_vect106:    b  hw_vect106
    .align 4
    hw_vect107:    b  hw_vect107
    .align 4
    hw_vect108:    b  hw_vect108
    .align 4
    hw_vect109:    b  hw_vect109
    .align 4
    hw_vect110:    b  hw_vect110
    .align 4
    hw_vect111:    b  hw_vect111
    .align 4
    hw_vect112:    b  hw_vect112
    .align 4
    hw_vect113:    b  hw_vect113
    .align 4
    hw_vect114:    b  hw_vect114
    .align 4
    hw_vect115:    b  hw_vect115
    .align 4
    hw_vect116:    b  hw_vect116
    .align 4
    hw_vect117:    b  hw_vect117
    .align 4
    hw_vect118:    b  hw_vect118
    .align 4
    hw_vect119:    b  hw_vect119
    .align 4
    hw_vect120:    b  hw_vect120
    .align 4
    hw_vect121:    b  hw_vect121
    .align 4
    hw_vect122:    b  hw_vect122
    .align 4
    hw_vect123:    b  hw_vect123
    .align 4
    hw_vect124:    b  hw_vect124
    .align 4
    hw_vect125:    b  hw_vect125
    .align 4
    hw_vect126:    b  hw_vect126
    .align 4
    hw_vect127:    b  hw_vect127
    .align 4
    hw_vect128:    b  hw_vect128
    .align 4
    hw_vect129:    b  hw_vect129
    .align 4
    hw_vect130:    b  hw_vect130
    .align 4
    hw_vect131:    b  hw_vect131
    .align 4
    hw_vect132:    b  hw_vect132
    .align 4
    hw_vect133:    b  hw_vect133
    .align 4
    hw_vect134:    b  hw_vect134
    .align 4
    hw_vect135:    b  hw_vect135
    .align 4
    hw_vect136:    b  hw_vect136
    .align 4
    hw_vect137:    b  hw_vect137
    .align 4
    hw_vect138:    b  hw_vect138
    .align 4
    hw_vect139:    b  hw_vect139
    .align 4
    hw_vect140:    b  hw_vect140
    .align 4
    hw_vect141:    b  hw_vect141
    .align 4
    hw_vect142:    b  hw_vect142
    .align 4
    hw_vect143:    b  hw_vect143
    .align 4
    hw_vect144:    b  hw_vect144
    .align 4
    hw_vect145:    b  hw_vect145
    .align 4
    hw_vect146:    b  hw_vect146
    .align 4
    hw_vect147:    b  hw_vect147
    .align 4
    hw_vect148:    b  hw_vect148
    .align 4
    hw_vect149:    b  hw_vect149
    .align 4
    hw_vect150:    b  hw_vect150
    .align 4
    hw_vect151:    b  hw_vect151
    .align 4
    hw_vect152:    b  hw_vect152
    .align 4
    hw_vect153:    b  hw_vect153
    .align 4
    hw_vect154:    b  hw_vect154
    .align 4
    hw_vect155:    b  hw_vect155
    .align 4
    hw_vect156:    b  hw_vect156
    .align 4
    hw_vect157:    b  eTimer00IntHandler
    .align 4
    hw_vect158:    b  eTimer01IntHandler
    .align 4
    hw_vect159:    b  eTimer02IntHandler
    .align 4
    hw_vect160:    b  hw_vect160
    .align 4
    hw_vect161:    b  hw_vect161
    .align 4
    hw_vect162:    b  hw_vect162
    .align 4
    hw_vect163:    b  hw_vect163
    .align 4
    hw_vect164:    b  hw_vect164
    .align 4
    hw_vect165:    b  hw_vect165
    .align 4
    hw_vect166:    b  hw_vect166
    .align 4
    hw_vect167:    b  hw_vect167
    .align 4
    hw_vect168:    b  hw_vect168
    .align 4
    hw_vect169:    b  hw_vect169
    .align 4
    hw_vect170:    b  hw_vect170
    .align 4
    hw_vect171:    b  hw_vect171
    .align 4
    hw_vect172:    b  hw_vect172
    .align 4
    hw_vect173:    b  hw_vect173
    .align 4
    hw_vect174:    b  hw_vect174
    .align 4
    hw_vect175:    b  hw_vect175
    .align 4
    hw_vect176:    b  hw_vect176
    .align 4
    hw_vect177:    b  hw_vect177
    .align 4
    hw_vect178:    b  hw_vect178
    .align 4
    hw_vect179:    b  hw_vect179
    .align 4
    hw_vect180:    b  hw_vect180
    .align 4
    hw_vect181:    b  hw_vect181
    .align 4
    hw_vect182:    b  hw_vect182
    .align 4
    hw_vect183:    b  hw_vect183
    .align 4
    hw_vect184:    b  hw_vect184
    .align 4
    hw_vect185:    b  hw_vect185
    .align 4
    hw_vect186:    b  hw_vect186
    .align 4
    hw_vect187:    b  hw_vect187
    .align 4
    hw_vect188:    b  hw_vect188
    .align 4
    hw_vect189:    b  hw_vect189
    .align 4
    hw_vect190:    b  hw_vect190
    .align 4
    hw_vect191:    b  hw_vect191
    .align 4
    hw_vect192:    b  hw_vect192
    .align 4
    hw_vect193:    b  hw_vect193
    .align 4
    hw_vect194:    b  hw_vect194
    .align 4
    hw_vect195:    b  hw_vect195
    .align 4
    hw_vect196:    b  hw_vect196
    .align 4
    hw_vect197:    b  hw_vect197
    .align 4
    hw_vect198:    b  hw_vect198
    .align 4
    hw_vect199:    b  hw_vect199
    .align 4
    hw_vect200:    b  hw_vect200
    .align 4
    hw_vect201:    b  hw_vect201
    .align 4
    hw_vect202:    b  hw_vect202
    .align 4
    hw_vect203:    b  hw_vect203
    .align 4
    hw_vect204:    b  hw_vect204
    .align 4
    hw_vect205:    b  hw_vect205
    .align 4
    hw_vect206:    b  hw_vect206
    .align 4
    hw_vect207:    b  hw_vect207
    .align 4
    hw_vect208:    b  hw_vect208
    .align 4
    hw_vect209:    b  hw_vect209
    .align 4
    hw_vect210:    b  hw_vect210
    .align 4
    hw_vect211:    b  hw_vect211
    .align 4
    hw_vect212:    b  hw_vect212
    .align 4
    hw_vect213:    b  hw_vect213
    .align 4
    hw_vect214:    b  hw_vect214
    .align 4
    hw_vect215:    b  hw_vect215
    .align 4
    hw_vect216:    b  hw_vect216
    .align 4
    hw_vect217:    b  hw_vect217
    .align 4
    hw_vect218:    b  hw_vect218
    .align 4
    hw_vect219:    b  hw_vect219
    .align 4
    hw_vect220:    b  hw_vect220
    .align 4
    hw_vect221:    b  hw_vect221
    .align 4
    hw_vect222:    b  hw_vect222
    .align 4
    hw_vect223:    b  hw_vect223
    .align 4
    hw_vect224:    b  hw_vect224
    .align 4
    hw_vect225:    b  hw_vect225
    .align 4
    hw_vect226:    b  hw_vect226
    .align 4
    hw_vect227:    b  hw_vect227
    .align 4
    hw_vect228:    b  hw_vect228
    .align 4
    hw_vect229:    b  hw_vect229
    .align 4
    hw_vect230:    b  hw_vect230
    .align 4
    hw_vect231:    b  hw_vect231
    .align 4
    hw_vect232:    b  hw_vect232
    .align 4
    hw_vect233:    b  hw_vect233
    .align 4
    hw_vect234:    b  hw_vect234
    .align 4
    hw_vect235:    b  hw_vect235
    .align 4
    hw_vect236:    b  hw_vect236
    .align 4
    hw_vect237:    b  hw_vect237
    .align 4
    hw_vect238:    b  hw_vect238
    .align 4
    hw_vect239:    b  hw_vect239
    .align 4
    hw_vect240:    b  hw_vect240
    .align 4
    hw_vect241:    b  hw_vect241
    .align 4
    hw_vect242:    b  hw_vect242
    .align 4
    hw_vect243:    b  hw_vect243
    .align 4
    hw_vect244:    b  hw_vect244
    .align 4
    hw_vect245:    b  hw_vect245
    .align 4
    hw_vect246:    b  hw_vect246
    .align 4
    hw_vect247:    b  hw_vect247
    .align 4
    hw_vect248:    b  hw_vect248
    .align 4
    hw_vect249:    b  hw_vect249
    .align 4
    hw_vect250:    b  hw_vect250
    .align 4
    hw_vect251:    b  hw_vect251
    .align 4
    hw_vect252:    b  hw_vect252
    .align 4
    hw_vect253:    b  hw_vect253
    .align 4
    hw_vect254:    b  hw_vect254
    .align 4
    hw_vect255:    b  hw_vect255

.size	_interrupt_table, .-_interrupt_table
