# awf -v outdir=d -v columns=c1:c2:.. -v files=f1:f2:... -f stat.awk
#
# calculates min, max, ave and std for each column and writes them to 
# the specified filesx
#   d/$(f1)_min.property, d/$(f1)_max.property, 
#   d/$(f1)_ave.property, d/$(f1)_stdlow.property, d/$(f1)_stdhigh.property
#   
#   d/#(f2)_...
# 
# @author istvan
#
BEGIN {
   cc = split(columns,C,/:/);
   fc = split(files,F,/:/);
   for(i=1; i<= fc; ++i) F[i] = outdir"/"F[i];
   for(i=fc+1; i<=cc; ++i) F[i] = outdir"/column_"C[i];
   n = 0;
}
{ 
   if (!NF) exit;
   for(i=1; i<= cc; ++i) {
      v = $(C[i]);   
      SS[i] += v*v;  
      S[i]  += v;
      if (n == 0 || v < MIN[i]) MIN[i] = v;
      if (n == 0 || v > MAX[i]) MAX[i] = v;
   }
   ++n;
} 
END {
   for(i=1; i<= cc; ++i) { 
      # We can calculate STD faster (and in a single shot) using
      # the identity:
      #    A = 1/n \sum_{i=1}^n x_i
      #    VAR = \sum_{i=1}^n (x_i - A)^2 == (\sum_{i=1}^n) - nA^2
      A = n ? S[i]/n : 0;
      STD = n > 1 ? sqrt((SS[i] - n*A*A)/(n-1)) : 0;
      Fmin=F[i]"_min.property";
      Fmax=F[i]"_max.property";
      Fave=F[i]"_ave.property";
      Flow=F[i]"_stdlow.property";
      Fhigh=F[i]"_stdhigh.property";

   
      printf("YVALUE=%.4lf\n",MIN[i]) > Fmin;
      printf("YVALUE=%.4lf\n",MAX[i]) > Fmax;
      printf("YVALUE=%.4lf\n",A) > Fave;
      printf("YVALUE=%.4lf\n",A-STD/2) > Flow;
      printf("YVALUE=%.4lf\n",A+STD/2) > Fhigh;
   }
}
