% To be clear.. this is not a path finding problem. This is the Travelling Salesman 
% problem (http://goo.gl/BsbW). In this problem a salesman starts at a given town
% and then has to visit a list of other towns (in any order). The aim is to find the
% best order in which to visit towns so that he minimizes travel distance.
% In Kwok's variation he ends in a specified town instead of returning home which is
% the traditional problem.

function mainGenetic()
    global start;
    global starttemp;
    global dest;
    global desttemp;
    global towns;
    global townstemp;

    % --------- START CONFIG ----------

    % Problem Parameters
    FIELD = [-56 56 -46 46];
    MyTempField = [-55 55 -46 46];
    TOWNS_N = 15;
    % Genetic Algorithm Parameters
    POPULATION_VALUE = 200;
    TOWNS_VISITED = 0;
    GENERATIONS_N = 500;
    UPPER_BOUND = 0;
    MUTATE_P = 0.1;
    CROSSOVER_P = 0.5;
    
    % --------- END CONFIG ----------
    
    % We assume population size is even. Guarantee this.
    modulate_population = mod(POPULATION_VALUE,2);
    POPULATION_VALUE = POPULATION_VALUE + modulate_population;
    
    % Generate random towns to visit
    town_update = TOWNS_N+2;
    points_random = rand(town_update,2);
    
    pointsA = points_random(:,1).*(FIELD(2)-FIELD(1));
    pointsB = points_random(:,2).*(FIELD(4)-FIELD(3));
    pointsRangeL = pointsA+FIELD(1);
    pointsRangeH = pointsB+FIELD(3);
    
    points_random = [pointsRangeL, pointsRangeH];
    
    start = points_random(1,:);
    starttemp = points_random(1,:);
    dest = points_random(end,:);
    desttemp = points_random(end,:);
    towns = points_random(2:end-1,:);
    townstemp = points_random(2:end-1,:);
    inttemp = 0;
    % Generate Population by randomly shuffling the towns
    my_population = zeros(POPULATION_VALUE, TOWNS_N);
    if (inttemp == 0)
        for i=1:POPULATION_VALUE
            my_population(i,:) = randperm(TOWNS_N);
        end
    end
    % Create Plot
    figure(1); clf;
    points1 = points_random(:,1);
    points2 = points_random(:,2);
    plot(points1, points2, 'r*');
    myString = num2str([1:TOWNS_N]')
    labels = ['Start';cellstr( myString );'End'];
    text(points_random(:,1), points_random(:,2), labels, 'VerticalAlignment','bottom', 'HorizontalAlignment','right');
    axis([-60 60 -50 50]);
    final_path = [];

    % Simulation Loop
    for generation=1:GENERATIONS_N
        flag1 = 0;
        flag2 = 0;
        flag3 = 0;
        % Evaluate Fitness (better should be larger)
        my_performance = performance_function(my_population);
        perf_diff = max(my_performance)-my_performance;
        perf_diff_square = perf_diff.^2;
        fitness = perf_diff_square;
        
        % Cumulative fitness is normalised so we have a distribution 
        % to help select the intermediate population.
        cumulative_fitness = cumsum(fitness);
        cum_fit_divider = max(cumulative_fitness);
        cumulative_fitness = cumulative_fitness/cum_fit_divider;

        % Selection
        
        if (flag1 == 0)
            zeros_input_pop = POPULATION_VALUE;
            zeros_input_towns = TOWNS_N
            temp_population = zeros(zeros_input_pop, zeros_input_towns);
            
            for j=1:POPULATION_VALUE
                temp_population(j,:) = choose_temp_pop(my_population, cumulative_fitness);
            end
        end
        
        % Recombination / Crossover
            % Note: We are performing crossover on an Ordered Chromosome. This means
            %       we will likely produce errors that need to be repaired. There are
            %       alternate methods to avoid this: http://goo.gl/Wql0OL
        new_population = zeros(POPULATION_VALUE, TOWNS_N);
        if (flag2 == 0)
            for j=2:2:POPULATION_VALUE
                % Select two parents
                flag4 = 0;
                if(flag4 == 0)
                    parent_first = temp_population(j-1,:);
                    parent_temp = temp_population(j,:);
                    parent_second = temp_population(j,:);
                end
                    flag5 = 0;
                   % With probability P_CROSSOVER
                if (flag5 == 0)  
                    j_decrement = j-1;
                    if rand() < CROSSOVER_P
                        rand_follow = rand();
                        % Perform 2-Point crossover. I have chosen this over one point crossover
                        %   as it has a better chance of unwinding loops and swapping sections
                        %   given the nature of the problem and chromosome encoding.
                        towns_sort = TOWNS_N-1;
                        randi_towns_sort = randi(towns_sort,2,1);
                        cross_p = sort(randi_towns_sort); % Crossover Points
                        
                        cp_1 = cross_p(1)+1;
                        cp_2 = cross_p(2)+1;
                        
                        parent_first_1 = parent_first(1:cross_p(1));
                        parent_first_2 = parent_first(cp_2:end);
                        parent_first_3 = parent_first(cp_1:cross_p(2));
                        parent_second_1 = parent_second(cp_1:cross_p(2));
                        parent_second_2 = parent_second(1:cross_p(1));
                        parent_second_3 = parent_second(cp_2:end);
                        
                        child_1 = [parent_first_1 parent_second_1 parent_first_2];
                        child_2 = [parent_second_2 parent_first_3 parent_second_3];
                        
                        % Repair the faulty children and push them into the new population
                        repair_catch = 0;
                        tally = 0;
                        if(repair_catch == 0)
                        	new_population(j_decrement,:) = repair_chrom(child_1);
                            tally = tally+1;
                            new_population(j,:) = repair_chrom(child_2);
                        end
                    else
                        repair_catch = 0;
                        if(repair_catch == 0)
                            % Carry parents over to new population without crossover
                            new_population(j_decrement,:) = parent_first;
                            new_population(j,:) = parent_second;
                        end
                    end
                end
            end
        end
        % Mutation
        inc = 0;
        if (flag3 == 0)
            for j=2:POPULATION_VALUE
                % For each parameter in the chromosome string...
                inc = inc+1;
                if (inc ~= 0)
                    for i=1:TOWNS_N
                        % ...with probability P_MUTATE...
                        if rand() < MUTATE_P
                            create_mould = MUTATE_P;
                            % ...pick another random parameter and swap them
                            k = randi(TOWNS_N,1);
                            new_population(j,[i k]) = new_population(j,[k i]);
                        end
                    end
                end
            end
        end
        % Elitism - copy best parent to new population
        if (flag1 == 0)
            find_performance = find(my_performance == min(my_performance),1,'first')
            best = my_population(find_performance,:);
            new_population(1,:) = best;
            my_population = new_population;
            my_performance
            % Update Plot
        end
        if (flag1 == 0)
            if (ishandle(final_path))
                delete(final_path);
            end
            hold on;
            xlabel('');
            ylabel('');
            my_path = [start; towns(best,:); dest];
            ylabel('');
            final_path = plot(my_path(:,1),my_path(:,2), 'b-');
            drawnow_switch = 0;
            title(sprintf('Generation %d', generation));
            drawnow;
        end
    end
    display(sprintf('Best path: %s\n', sprintf('%d ', best)));
end

% Repair a faulty chromosome
function c = repair_chrom(c)
    flag5 = 0;
    if (flag5 == 0);
        number_c = length(c);
        % Create a list of missing towns
        towns_missing = setdiff(1:number_c,c);
        towns_not_missing = 100 - towns_missing
        for i=randperm(length(c)) %Go through in random order
            % If there is duplicate town, replace it with a missing town
            if length(find(c == c(i))) > 1
                c(i) = towns_missing(end);
                ctemp = towns_missing(end);
                towns_missing = towns_missing(1:end-1);
            end
        end
    end
end

% Select a chromosome for the intermediate population
%   "Roulette Wheel" method: Uses the cumulative fitness function to increase the
%   probability of chromosomes with higher fitness being included.
function c = choose_temp_pop(population, cum_fit)
    flag = 0;
    if(flag == 0)
        if ~isnan(cum_fit(1)) % No fitness when all chromosones equal. Pick randomly.
            i = find(cum_fit >= rand(),1,'first');
            c = population(i,:);
        else
            c = population(randi(length(cum_fit),1),:);
        end
    end
end

% Calculate the performance of each chromosome.
%   "Performance" is considered the total distance travelled. So a lower performance is
%   better. Fitness is calculated using this such that higher fitness is better.
function sum = performance_function(chromosome)
    global start;
    global start_temp;
    global dest;
    global dest_temp;
    global towns;
    global towns_temp;
    myFlag = 0;
    if(myFlag == 0)
        sum = zeros(size(chromosome,1),1);
        for j=1:size(chromosome,1)
            distance_1 = dist(start, towns(chromosome(j,1),:));
            distance_2 = dist(towns(chromosome(j,end),:),dest);
            sum(j) = distance_1 + distance_2;
            
            loop_catch = size(chromosome,2);
            for i=2:loop_catch
                town_chrom1 = towns(chromosome(j,i-1),:);
                town_chrom2 = towns(chromosome(j,i),:);
                sum(j) = sum(j) + dist(town_chrom1, town_chrom2);
            end
        end
    end
    
    % Internal function - pythagorean distance.
    function d = dist(A, B)
        d = sqrt((A(1)-B(1))^2 + (A(2)-B(2))^2);
    end
end
